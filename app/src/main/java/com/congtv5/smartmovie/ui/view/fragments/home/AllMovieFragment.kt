package com.congtv5.smartmovie.ui.view.fragments.home

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.view.adapter.MovieSectionListAdapter
import com.congtv5.smartmovie.ui.view.model.MovieSection
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AllMovieFragment : BaseFragment() {

    companion object {
        const val ITEM_PER_SECTION = 4
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[HomeViewModel::class.java]
    }

    private var movieGridListAdapter: MovieSectionListAdapter? = null
    private var movieLinearListAdapter: MovieSectionListAdapter? = null

    private lateinit var movieListRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun getLayoutID(): Int {
        return R.layout.fragment_all_movie
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        movieListRecyclerView = view.findViewById(R.id.rvMovieList)
        swipeRefreshLayout = view.findViewById(R.id.rlRefresh)
    }

    override fun initObserveData() {

        homeViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.movieSectionMap },
            observer = { sectionMap ->
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

        // listen favorite List from db change
        lifecycleScope.launchWhenStarted {
            homeViewModel.store.state.favoriteList?.collect { list ->
                homeViewModel.setFavoriteList(list) // update fav list
                homeViewModel.applyFavoriteToAllMovie(list) // apply fav list to movie list from network
            }
        }

    }

    override fun initData() {
    }

    override fun initView() {
        initSectionAdapter()
    }

    override fun initAction() {
        swipeRefreshLayout.setOnRefreshListener {
            reloadData()
        }
    }

    private fun initSectionAdapter() {
        movieListRecyclerView.layoutManager = LinearLayoutManager(context)
        movieGridListAdapter =
            MovieSectionListAdapter(MovieItemDisplayType.GRID, { movieId ->
                goToMovieDetailPage(movieId)
            }, { movieCategory ->
                goToOtherCategory(movieCategory)
            }, { favMovie ->
                updateFavoriteMovie(favMovie)
            })
        movieLinearListAdapter =
            MovieSectionListAdapter(MovieItemDisplayType.VERTICAL_LINEAR, { movieId ->
                goToMovieDetailPage(movieId)
            }, { movieCategory ->
                goToOtherCategory(movieCategory)
            }, { favMovie ->
                updateFavoriteMovie(favMovie)
            })
    }

    private fun updateSections(sectionMap: Map<MovieCategory, Resource<MovieListPage>?>) {
        // get success loaded section
        val sectionList = sectionMap.filter { item ->
            item.value is Resource.Success
        }.map { item ->
            val listForSection = getListForSection(item.value?.data?.results ?: listOf())
            MovieSection(item.key, listForSection)
        }
        movieGridListAdapter?.submitList(sectionList)
        movieLinearListAdapter?.submitList(sectionList)
    }

    private fun getListForSection(movieList: List<Movie>): List<Movie> {
        return if (movieList.size >= ITEM_PER_SECTION) {
            movieList.subList(0, ITEM_PER_SECTION)
        } else {
            // results from network less than 4
            movieList
        }
    }

    private fun updateDisplayType(type: MovieItemDisplayType) {
        when (type) {
            MovieItemDisplayType.GRID -> {
                movieListRecyclerView.adapter = movieGridListAdapter
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                movieListRecyclerView.adapter = movieLinearListAdapter
            }
        }
    }

    private fun updateFavoriteMovie(favoriteMovie: FavoriteMovie) {
        homeViewModel.updateFavoriteMovie(favoriteMovie)
    }

    private fun reloadData() {
        homeViewModel.getMovieListToInitAllPage()
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment2(movieId)
        findNavController().navigate(action)
    }

    private fun goToOtherCategory(movieCategory: MovieCategory) {
        homeViewModel.setCurrentPageType(movieCategory)
    }

}