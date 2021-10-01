package com.congtv5.smartmovie.ui.base.fragment

import android.util.Log
import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.viewmodel.BaseMovieListViewModel
import com.congtv5.smartmovie.ui.view.adapter.MovieListAdapter
import com.congtv5.smartmovie.ui.view.fragments.home.HomeFragment.Companion.GRID_ITEM_PER_ROW
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType

abstract class MovieListFragment : BaseFragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var movieListViewModel: BaseMovieListViewModel

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

    //    override fun getLayoutID(): Int {
//        return R.layout.fragment_popular_movie
//    }
    abstract fun initViewModel()
    abstract fun getFirstMovieListPage(): MovieListPage?
    abstract fun goToMovieDetailPage(movieId: Int)

    override fun initBinding(view: View) {
        rvMovieList = view.findViewById(R.id.rvMovieList)
        prbLoadMore = view.findViewById(R.id.prbLoadMore)
        rlRefresh = view.findViewById(R.id.rlRefresh)
    }

    override fun initObserveData() {
        initViewModel()
        //shared viewModel to change displayType
        homeViewModel.store.observe(
            owner = this,
            selector = { state -> state.currentDisplayType },
            observer = { type ->
                updateDisplayType(type)
            }
        )

        movieListViewModel.store.observe(
            owner = this,
            selector = { state -> state.isReloading },
            observer = { isReloading ->
                if (!isReloading) rlRefresh.isRefreshing = false
            }
        )

        movieListViewModel.store.observe(
            owner = this,
            selector = { state -> state.isLoadingMore },
            observer = { isLoadingMore ->
                loadingMore(isLoadingMore)
            }
        )

        movieListViewModel.store.observe(
            owner = this,
            selector = { state -> state.movieListPages },
            observer = { movieListPagers ->
                updateMovieList(movieListPagers)
            }
        )
    }

    private fun loadingMore(isLoadingMore: Boolean) {
        if (isLoadingMore) {
            prbLoadMore.visibility = View.VISIBLE
        } else {
            prbLoadMore.visibility = View.GONE
        }
    }

    override fun initData() {
        // get item from first load
        getFirstMovieListPage()?.let { movieListPage ->
            movieListViewModel.addMovieListPage(movieListPage)
            movieListViewModel.setCurrentPage(1)
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
        movieListViewModel.clearData()
        movieListViewModel.setIsReloading(true)
        movieListViewModel.getNextMovieListPage()
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
                    if (isLoadingMorePosition() && !movieListViewModel.store.state.isLoadingMore) {
                        movieListViewModel.getNextMovieListPage()
                    }
                }
            }
        })
    }

    private fun isLoadingMorePosition(): Boolean {
        // calculate correct position
        val listOfMovieList = movieListViewModel.store.state.movieListPages
            .map { it.results.size }
        Log.d("CongTV5", "MovieListFragment #isLoadingMorePosition $listOfMovieList")
        if (listOfMovieList.isEmpty()) return false
        totalItemNumber = listOfMovieList.reduce { accumulator, value -> accumulator + value }
        return isScrolling && scrollOutItemNumber + GRID_ITEM_PER_ROW >= totalItemNumber
    }

    private fun updateDisplayType(type: MovieItemDisplayType) {
        Log.d("CongTV5", "MovieListFragment #updateDisplayType $type")
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
        val movieList = mutableListOf<Movie>()
        movieListPagers.forEach { movieList.addAll(it.results) }
        movieGridListAdapter?.submitList(movieList)
        movieLinearListAdapter?.submitList(movieList)
    }

    private fun updateFavoriteMovie(favoriteMovie: FavoriteMovie) {
        homeViewModel.updateFavoriteMovie(favoriteMovie)
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

//    private fun goToMovieDetailPage(movieId: Int) {
//        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment2(movieId)
//        findNavController().navigate(action)
//    }
}