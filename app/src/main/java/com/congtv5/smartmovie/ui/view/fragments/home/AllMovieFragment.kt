package com.congtv5.smartmovie.ui.view.fragments.home

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.data.model.ui.MovieSection
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.MovieSectionListAdapter
import com.congtv5.smartmovie.ui.viewmodel.AllMovieListViewModel
import com.congtv5.smartmovie.ui.viewmodel.HomeViewModel
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.flow.collect

class AllMovieFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val allMovieListViewModel: AllMovieListViewModel by viewModels()

    private var movieGridListAdapter: MovieSectionListAdapter? = null
    private var movieLinearListAdapter: MovieSectionListAdapter? = null

    private lateinit var rvMovieList: RecyclerView
    private lateinit var rlRefresh: SwipeRefreshLayout

    override fun getLayoutID(): Int {
        return R.layout.fragment_all_movie
    }

    override fun initBinding(view: View) {
        rvMovieList = view.findViewById(R.id.rvMovieList)
        rlRefresh = view.findViewById(R.id.rlRefresh)
    }

    override fun initObserveData() {

        lifecycleScope.launchWhenResumed {
            allMovieListViewModel.isLoading.collect { isLoading ->
                rlRefresh.isRefreshing = isLoading
            }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.allMovieSections.collect { sectionMap ->
                updateReloadingBarBySectionMap(sectionMap)
                updateSections(sectionMap)
            }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.currentDisplayType.collect { type ->
                updateDisplayType(type)
            }
        }
    }


    override fun initData() {
    }

    override fun initView() {
        initSectionAdapter()
    }

    override fun initAction() {
        rlRefresh.setOnRefreshListener {
            reloadData()
        }
    }

    private fun initSectionAdapter() {
        rvMovieList.layoutManager = LinearLayoutManager(context)
        movieGridListAdapter =
            MovieSectionListAdapter(MovieItemDisplayType.GRID, { movieId ->
                goToMovieDetailPage(movieId)
            }, { movieCategory ->
                goToOtherCategory(movieCategory)
            }, { favMovie ->
                updateFavoriteMovie(favMovie)
            }, { movieId ->
                isMovieFavorite(movieId)
            })
        movieLinearListAdapter =
            MovieSectionListAdapter(MovieItemDisplayType.VERTICAL_LINEAR, { movieId ->
                goToMovieDetailPage(movieId)
            }, { movieCategory ->
                goToOtherCategory(movieCategory)
            }, { favMovie ->
                updateFavoriteMovie(favMovie)
            }, { movieId ->
                isMovieFavorite(movieId)
            })
    }

    private fun updateSections(sectionMap: Map<MovieCategory, Resource<List<Result>>?>) {
        // get success loaded section
        val sectionList = sectionMap.filter { item ->
            item.value is Resource.Success
        }.map { item ->
            MovieSection(item.key, item.value?.data ?: listOf())
        }
        movieGridListAdapter?.submitList(sectionList)
        movieLinearListAdapter?.submitList(sectionList)
    }

    private fun updateDisplayType(type: MovieItemDisplayType) {
        when (type) {
            MovieItemDisplayType.GRID -> {
                rvMovieList.adapter = movieGridListAdapter
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                rvMovieList.adapter = movieLinearListAdapter
            }
        }
    }

    private fun updateFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        homeViewModel.updateFavoriteMovie(favoriteMovieEntity)
    }

    private fun isMovieFavorite(movieId: Int): Boolean {
        return homeViewModel.isMovieFavorite(movieId)
    }

    private fun reloadData(){
        allMovieListViewModel.setIsReloading(true)
        homeViewModel.clearAllData()
        homeViewModel.getMovieListToInitAllPage()
    }

    private fun updateReloadingBarBySectionMap(allMovieSections: Map<MovieCategory, Resource<List<Result>>?>) {
        // for loading
        val isLoadingDone = allMovieSections.filter { item ->
            item.value == null
        }.isEmpty()
        if (isLoadingDone) {
            allMovieListViewModel.setIsReloading(false)
        }
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment2(movieId)
        findNavController().navigate(action)
    }

    private fun goToOtherCategory(movieCategory: MovieCategory) {
        homeViewModel.setCurrentPage(movieCategory)
    }

}