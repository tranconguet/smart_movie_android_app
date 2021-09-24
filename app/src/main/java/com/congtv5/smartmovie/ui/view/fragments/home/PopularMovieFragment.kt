package com.congtv5.smartmovie.ui.view.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.databinding.FragmentPopularMovieBinding
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.MovieListAdapter
import com.congtv5.smartmovie.ui.viewmodel.HomeViewModel
import com.congtv5.smartmovie.ui.viewmodel.MainActivityViewModel
import com.congtv5.smartmovie.ui.viewmodel.PopularListViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PopularMovieFragment : BaseFragment<FragmentPopularMovieBinding>() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val popularViewModel: PopularListViewModel by viewModels()

    private var isScrolling = false
    private var totalItemNumber = 0
    private var scrollOutItemNumber = 0

    private var movieGridListAdapter: MovieListAdapter? = null
    private var movieLinearListAdapter: MovieListAdapter? = null

    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPopularMovieBinding {
        return FragmentPopularMovieBinding.inflate(inflater, container, false)
    }

    override fun initObserveData() {

        lifecycleScope.launchWhenResumed {
            popularViewModel.popularMovieListPages.collect { movieListPagers ->
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
            binding.prbLoadMore.visibility = View.VISIBLE
        } else {
            binding.prbLoadMore.visibility = View.GONE
        }
    }

    override fun initData() {
        homeViewModel.popularMovieListPages.value?.let { movieListPage ->
            popularViewModel.addMovieListPage(movieListPage) // get item from first load
        }
    }

    override fun initView() {
        initMovieListAdapter()
    }

    override fun initAction() {
        initScrollAction()
    }

    private fun initScrollAction() {
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
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
        val currentLayoutManager = binding.rvMovieList.layoutManager
        scrollOutItemNumber = when (currentLayoutManager) {
            is LinearLayoutManager -> (currentLayoutManager as? LinearLayoutManager?)?.findLastVisibleItemPosition()
                ?: 0
            else -> (currentLayoutManager as? GridLayoutManager?)?.findLastVisibleItemPosition()
                ?: 0
        }
        totalItemNumber = popularViewModel.popularMovieListPages.value
            .map { it.results.size }
            .reduce { accumulator, value -> accumulator + value }
        return isScrolling && scrollOutItemNumber + 2 >= totalItemNumber
    }

    private fun updateDisplayType(type: MovieItemDisplayType) {
        when (type) {
            MovieItemDisplayType.GRID -> {
                binding.rvMovieList.adapter = movieGridListAdapter
                binding.rvMovieList.layoutManager = gridLayoutManager
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                binding.rvMovieList.adapter = movieLinearListAdapter
                binding.rvMovieList.layoutManager = linearLayoutManager
            }
        }
    }

    private fun updateMovieList(movieListPagers: List<MovieListPage>) {
        val movieList = mutableListOf<Result>()
        movieListPagers.forEach { movieList.addAll(it.results) }
        movieGridListAdapter?.submitList(movieList)
        movieLinearListAdapter?.submitList(movieList)
    }

    private fun initMovieListAdapter() {
        context?.let { context ->
            movieGridListAdapter =
                MovieListAdapter(MovieItemDisplayType.GRID, context) { movieId: Int ->
                    goToMovieDetailPage(movieId)
                }
            movieLinearListAdapter =
                MovieListAdapter(MovieItemDisplayType.VERTICAL_LINEAR, context) { movieId: Int ->
                    goToMovieDetailPage(movieId)
                }

            gridLayoutManager = GridLayoutManager(context, 2)
            linearLayoutManager = LinearLayoutManager(context)
        }
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = HomeFragmentDirections.actionFHomeToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

}