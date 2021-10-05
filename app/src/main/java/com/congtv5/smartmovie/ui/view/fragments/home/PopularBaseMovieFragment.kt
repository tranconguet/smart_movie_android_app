package com.congtv5.smartmovie.ui.view.fragments.home

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseMovieListFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.ui.viewmodel.home.PopularListViewModel
import com.congtv5.smartmovie.utils.MovieCategory
import javax.inject.Inject

class PopularBaseMovieFragment : BaseMovieListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val myHomeViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[HomeViewModel::class.java]
    }

    @Inject
    lateinit var popularListViewModel: PopularListViewModel

    override fun getLayoutID(): Int {
        return R.layout.fragment_popular_movie
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initViewModel() {
        homeViewModel = myHomeViewModel
        movieListViewModel = popularListViewModel
    }

    override fun getFirstMovieListPage(): MovieListPage? {
        return homeViewModel.currentState.movieSectionMap[MovieCategory.POPULAR]?.data
    }

    override fun goToMovieDetailPage(movieId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment2(movieId)
        findNavController().navigate(action)
    }

}