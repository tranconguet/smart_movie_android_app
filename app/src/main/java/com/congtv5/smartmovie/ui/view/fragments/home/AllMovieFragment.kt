package com.congtv5.smartmovie.ui.view.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.data.model.ui.MovieSection
import com.congtv5.smartmovie.databinding.FragmentAllMovieBinding
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.MovieSectionListAdapter
import com.congtv5.smartmovie.ui.viewmodel.HomeViewModel
import com.congtv5.smartmovie.ui.viewmodel.MainActivityViewModel
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.flow.collect

class AllMovieFragment : BaseFragment<FragmentAllMovieBinding>() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private var movieGridListAdapter: MovieSectionListAdapter? = null
    private var movieLinearListAdapter: MovieSectionListAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAllMovieBinding {
        return FragmentAllMovieBinding.inflate(inflater, container, false)
    }

    override fun initObserveData() {

        lifecycleScope.launchWhenResumed {
            homeViewModel.allMovieSections.collect { sectionMap ->
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
    }

    private fun initSectionAdapter() {
        binding.rvMovieList.layoutManager = LinearLayoutManager(context)
        context?.let { context ->
            movieGridListAdapter =
                MovieSectionListAdapter(MovieItemDisplayType.GRID, context) { movieId ->
                    goToMovieDetailPage(movieId)
                }
            movieLinearListAdapter =
                MovieSectionListAdapter(MovieItemDisplayType.VERTICAL_LINEAR, context) { movieId ->
                    goToMovieDetailPage(movieId)
                }
        }
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
                binding.rvMovieList.adapter = movieGridListAdapter
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                binding.rvMovieList.adapter = movieLinearListAdapter
            }
        }
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = HomeFragmentDirections.actionFHomeToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

}