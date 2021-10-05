package com.congtv5.smartmovie.ui.base.fragment

import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collect

abstract class BaseMovieListFragment : BaseFragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var movieListViewModel: BaseMovieListViewModel

    private lateinit var rvMovieList: RecyclerView
    private lateinit var prbLoadMore: ProgressBar
    private lateinit var rlRefresh: SwipeRefreshLayout
    private lateinit var prbLoading: ProgressBar
    private lateinit var layoutError: LinearLayout
    private lateinit var tvReload: TextView

    // calculate for load more
    private var isScrolling = false
    private var totalItemNumber = 0
    private var scrollOutItemNumber = 0

    private var movieGridListAdapter: MovieListAdapter? = null
    private var movieLinearListAdapter: MovieListAdapter? = null

    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    abstract fun initViewModel()
    abstract fun getFirstMovieListPage(): MovieListPage?
    abstract fun goToMovieDetailPage(movieId: Int)

    override fun initBinding(view: View) {
        rvMovieList = view.findViewById(R.id.rvMovieList)
        prbLoadMore = view.findViewById(R.id.prbLoadMore)
        rlRefresh = view.findViewById(R.id.rlRefresh)
        prbLoading = view.findViewById(R.id.prbLoading)
        layoutError = view.findViewById(R.id.layoutError)
        tvReload = view.findViewById(R.id.tvReload)
    }

    override fun initObserveData() {
        // shared viewModel to change displayType
        homeViewModel.store.observeAnyway(
            owner = viewLifecycleOwner,
            selector = { state -> state.currentDisplayType },
            observer = { type ->
                updateDisplayType(type)
            }
        )
        // listen favorite List from db change
        lifecycleScope.launchWhenStarted {
            homeViewModel.store.state.favoriteList?.collect { list ->
                homeViewModel.setFavoriteList(list) // update fav list to db
                movieListViewModel.applyFavoriteToAllMovie(list) // apply fav list from db to movie list from network
            }
        }

        movieListViewModel.store.observeAnyway(
            owner = viewLifecycleOwner,
            selector = { state -> state.movieListPages },
            observer = { movieListPagers ->
                updateMovieList(movieListPagers)
            }
        )

        movieListViewModel.store.observeAnyway(
            owner = viewLifecycleOwner,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                handleLoading(isLoading)
            }
        )

        movieListViewModel.store.observeAnyway(
            owner = viewLifecycleOwner,
            selector = { state -> state.isLoadingMore },
            observer = { isLoadingMore ->
                handleLoadingMore(isLoadingMore)
            }
        )

    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            prbLoading.visibility = View.VISIBLE
            layoutError.visibility = View.INVISIBLE
        } else if (!loading && movieListViewModel.currentState.isError) {
            rlRefresh.isRefreshing = false
            prbLoading.visibility = View.INVISIBLE
            layoutError.visibility = View.VISIBLE
        } else {
            rlRefresh.isRefreshing = false
            prbLoading.visibility = View.INVISIBLE
            layoutError.visibility = View.INVISIBLE
        }
    }

    override fun initData() {
        initViewModel()
        // get item from home page
        if (movieListViewModel.store.state.currentPage == 0) {
            getFirstMovieListPage()?.let { movieListPage ->
                movieListViewModel.addMovieListPage(movieListPage)
                movieListViewModel.setCurrentPage(1)
            }
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
        tvReload.setOnClickListener {
            reloadData()
        }
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
                        // trigger load more
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
        if (listOfMovieList.isEmpty()) return false
        totalItemNumber = listOfMovieList.reduce { accumulator, value -> accumulator + value }
        return isScrolling && scrollOutItemNumber + GRID_ITEM_PER_ROW >= totalItemNumber
    }

    private fun initMovieListAdapter() {
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

    private fun reloadData() {
        movieListViewModel.clearData()
        movieListViewModel.getNextMovieListPage()
    }

    private fun handleLoadingMore(isLoadingMore: Boolean) {
        if (isLoadingMore) {
            prbLoadMore.visibility = View.VISIBLE
        } else {
            prbLoadMore.visibility = View.GONE
        }
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

}