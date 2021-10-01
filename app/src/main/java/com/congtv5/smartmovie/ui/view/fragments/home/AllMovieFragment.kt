package com.congtv5.smartmovie.ui.view.fragments.home

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.view.adapter.MovieSectionListAdapter
import com.congtv5.smartmovie.ui.view.model.MovieSection
import com.congtv5.smartmovie.ui.viewmodel.home.AllMovieListViewModel
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import javax.inject.Inject

class AllMovieFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[HomeViewModel::class.java]
    }

    @Inject
    lateinit var allMovieListViewModel: AllMovieListViewModel

    private var movieGridListAdapter: MovieSectionListAdapter? = null
    private var movieLinearListAdapter: MovieSectionListAdapter? = null

    private lateinit var rvMovieList: RecyclerView
    private lateinit var rlRefresh: SwipeRefreshLayout

    override fun getLayoutID(): Int {
        return R.layout.fragment_all_movie
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        rvMovieList = view.findViewById(R.id.rvMovieList)
        rlRefresh = view.findViewById(R.id.rlRefresh)
    }

    override fun initObserveData() {

        allMovieListViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                rlRefresh.isRefreshing = isLoading
            }
        )

        homeViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.movieSectionMap },
            observer = { sectionMap ->
                updateReloadingBarBySectionMap(sectionMap)
                updateSections(sectionMap)
            }
        )

        homeViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.currentDisplayType },
            observer = { type ->
                updateDisplayType(type)
            }
        )

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

    private fun updateSections(sectionMap: Map<MovieCategory, Resource<List<Movie>>?>) {
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

    private fun updateFavoriteMovie(favoriteMovie: FavoriteMovie) {
        homeViewModel.updateFavoriteMovie(favoriteMovie)
    }

    private fun isMovieFavorite(movieId: Int): Boolean {
        return homeViewModel.isMovieFavorite(movieId)
    }

    private fun reloadData() {
        allMovieListViewModel.setIsReloading(true)
        homeViewModel.clearAllData()
        homeViewModel.getMovieListToInitAllPage()
    }

    private fun updateReloadingBarBySectionMap(allMovieSections: Map<MovieCategory, Resource<List<Movie>>?>) {
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
        homeViewModel.setCurrentPageType(movieCategory)
    }

}