package com.congtv5.smartmovie.ui.view.fragments.home

import android.util.Log
import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.MovieListAdapter
import com.congtv5.smartmovie.ui.view.fragments.home.HomeFragment.Companion.GRID_ITEM_PER_ROW
import com.congtv5.smartmovie.ui.viewmodel.HomeViewModel
import com.congtv5.smartmovie.ui.viewmodel.PopularListViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PopularMovieFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val popularViewModel: PopularListViewModel by viewModels()

    private lateinit var rvMovieList: RecyclerView
    private lateinit var prbLoadMore: ProgressBar
    private lateinit var rlRefresh: SwipeRefreshLayout

    private var isScrolling = false // for load more
    private var totalItemNumber = 0
    private var scrollOutItemNumber = 0

    private var movieGridListAdapter: MovieListAdapter? = null
    private var movieLinearListAdapter: MovieListAdapter? = null

    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun getLayoutID(): Int {
        return R.layout.fragment_popular_movie
    }

    override fun initBinding(view: View) {
        rvMovieList = view.findViewById(R.id.rvMovieList)
        prbLoadMore = view.findViewById(R.id.prbLoadMore)
        rlRefresh = view.findViewById(R.id.rlRefresh)
    }

    override fun initObserveData() {

        lifecycleScope.launchWhenResumed {
            popularViewModel.isReloading.collect { isReloading ->
                if (!isReloading) rlRefresh.isRefreshing = false
            }
        }

        lifecycleScope.launchWhenResumed {
            popularViewModel.popularMovieListPages.collect { movieListPagers ->
                Log.d("CongTV5", "PopularMovieFragment #initObserveData ${movieListPagers.size}")
                updateMovieList(movieListPagers)
            }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.currentDisplayType.collect { type ->
                updateDisplayType(type)
            }
        }

        lifecycleScope.launchWhenResumed {
            popularViewModel.isLoadingMore.collect { isLoadingMore ->
                loadingMore(isLoadingMore)
            }
        }

    }

    private fun loadingMore(isLoadingMore: Boolean) {
        if (isLoadingMore) {
            prbLoadMore.visibility = View.VISIBLE
        } else {
            prbLoadMore.visibility = View.GONE
        }
    }

    override fun initData() {
        homeViewModel.popularMovieListPages.value?.let { movieListPage ->
            popularViewModel.addMovieListPage(movieListPage) // get item from first load
            popularViewModel.setCurrentPage(1)
        }
    }

    override fun initView() {
        initMovieListAdapter()
    }

    override fun initAction() {
        initScrollAction()
        rlRefresh.setOnRefreshListener {
            reloadData()
        }
    }

    private fun reloadData() {
        initScrollAction()
        popularViewModel.clearData()
        popularViewModel.setIsReloading(true)
        popularViewModel.getNextMovieListPage()
    }

    private fun initScrollAction() {
        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentLayoutManager = rvMovieList.layoutManager
                scrollOutItemNumber = when (currentLayoutManager) {
                    is LinearLayoutManager -> (currentLayoutManager as? LinearLayoutManager?)?.findLastVisibleItemPosition()
                        ?: 0
                    else -> (currentLayoutManager as? GridLayoutManager?)?.findLastVisibleItemPosition()
                        ?: 0
                }
                if (scrollOutItemNumber != -1) {
                    if (isLoadingMorePosition() && !popularViewModel.isLoadingMore.value) {
                        popularViewModel.getNextMovieListPage()
                    }
                }
            }
        })
    }

    private fun isLoadingMorePosition(): Boolean {
        // calculate correct position
        val listOfMovieList = popularViewModel.popularMovieListPages.value
            .map { it.results.size }
        Log.d("CongTV5", "PopularMovieFragment #isLoadingMorePosition $listOfMovieList")
        if (listOfMovieList.isEmpty()) return false
        totalItemNumber = listOfMovieList.reduce { accumulator, value -> accumulator + value }
        return isScrolling && scrollOutItemNumber + GRID_ITEM_PER_ROW >= totalItemNumber
    }

    private fun updateDisplayType(type: MovieItemDisplayType) {
        when (type) {
            MovieItemDisplayType.GRID -> {
                rvMovieList.adapter = movieGridListAdapter
                rvMovieList.layoutManager = gridLayoutManager
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                rvMovieList.adapter = movieLinearListAdapter
                rvMovieList.layoutManager = linearLayoutManager
            }
        }
    }

    private fun updateMovieList(movieListPagers: List<MovieListPage>) {
        val movieList = mutableListOf<Result>()
        movieListPagers.forEach { movieList.addAll(it.results) }
        movieGridListAdapter?.submitList(movieList)
        movieLinearListAdapter?.submitList(movieList)
    }

    private fun updateFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        homeViewModel.updateFavoriteMovie(favoriteMovieEntity)
    }

    private fun isMovieFavorite(movieId: Int): Boolean {
        return homeViewModel.isMovieFavorite(movieId)
    }

    private fun initMovieListAdapter() {
        context?.let { context ->
            movieGridListAdapter =
                MovieListAdapter(MovieItemDisplayType.GRID, { movieId: Int ->
                    goToMovieDetailPage(movieId)
                }, { favMovie ->
                    updateFavoriteMovie(favMovie)
                }, { movieId ->
                    isMovieFavorite(movieId)
                })
            movieLinearListAdapter =
                MovieListAdapter(MovieItemDisplayType.VERTICAL_LINEAR, { movieId: Int ->
                    goToMovieDetailPage(movieId)
                }, { favMovie ->
                    updateFavoriteMovie(favMovie)
                }, { movieId ->
                    isMovieFavorite(movieId)
                })

            gridLayoutManager = GridLayoutManager(context, GRID_ITEM_PER_ROW)
            linearLayoutManager = LinearLayoutManager(context)
        }
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment2(movieId)
        findNavController().navigate(action)
    }

}