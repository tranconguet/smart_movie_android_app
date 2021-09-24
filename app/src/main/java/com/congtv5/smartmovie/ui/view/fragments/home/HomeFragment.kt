package com.congtv5.smartmovie.ui.view.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.databinding.FragmentHomeBinding
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.MovieListTypePagerAdapter
import com.congtv5.smartmovie.ui.viewmodel.HomeViewModel
import com.congtv5.smartmovie.ui.viewmodel.MainActivityViewModel
import com.congtv5.smartmovie.utils.Constants.MOVIES_TEXT
import com.congtv5.smartmovie.utils.Constants.movieListTypeNameList
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.Resource
import com.congtv5.smartmovie.utils.getFragmentByMovieCategory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initObserveData() {

        lifecycleScope.launchWhenResumed {
            homeViewModel.isLoading.collect { isLoading ->
                setUpProgressBar(isLoading)
            }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.isError.collect { isError ->
                setUpError(isError)
            }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.allMovieSections.collect { allMovieSections ->
                updateLoadingProgressBar(allMovieSections)
                updateMovieSectionTab(allMovieSections)
            }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.currentDisplayType.collect { type ->
                when (type) {
                    MovieItemDisplayType.GRID -> {
                        binding.tvDisplayType.setImageResource(R.drawable.linear_display)
                    }
                    MovieItemDisplayType.VERTICAL_LINEAR -> {
                        binding.tvDisplayType.setImageResource(R.drawable.grid_display)
                    }
                }
            }
        }
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initAction() {

        binding.tvReload.setOnClickListener {
            reloadInitData()
        }

        binding.tvDisplayType.setOnClickListener {
            toggleDisplayType()
        }
    }

    private fun updateLoadingProgressBar(allMovieSections: Map<MovieCategory, Resource<List<Result>>?>) {
        // for loading
        val isLoadingDone = allMovieSections.filter { item ->
            item.value == null
        }.isNotEmpty()
        homeViewModel.setIsLoading(isLoadingDone)
    }

    private fun updateMovieSectionTab(allMovieSections: Map<MovieCategory, Resource<List<Result>>?>) {
        // prepare data to show
        // movies tab is default
        val fragments = mutableListOf<Fragment>(AllMovieFragment())
        val fragmentNames = mutableListOf(MOVIES_TEXT)

        allMovieSections.filter { item ->
            item.value is Resource.Success
        }.keys.forEach { movieCategory ->
            fragments.add(getFragmentByMovieCategory(movieCategory))
        }

        allMovieSections.filter { item ->
            item.value is Resource.Success
        }.keys.forEach { movieCategory ->
            fragmentNames.add(movieCategory.text)
        }

        if (fragments.size > 1 && !homeViewModel.isLoading.value) { //at least 1 page call success
            initViewPager(fragments, fragmentNames)
            showAllPage()
            homeViewModel.setIsError(false)
        } else if (!homeViewModel.isLoading.value) { // loading done but nothing success
            homeViewModel.setIsError(true)
        }

    }

    private fun showAllPage() {
        binding.layoutHome.visibility = View.VISIBLE
    }

    private fun reloadInitData() {
        if (homeViewModel.isError.value) {
            homeViewModel.setIsError(false)
            loadInitData()
        }
    }

    private fun loadInitData() {
        homeViewModel.clearSectionMap()
        homeViewModel.getMovieListToInitAllPage()
    }

    private fun setUpError(isError: Boolean) {
        if (isError) {
            binding.errorNotification.visibility = View.VISIBLE
        } else {
            binding.errorNotification.visibility = View.INVISIBLE
        }
    }

    private fun setUpProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.prbMain.visibility = View.VISIBLE
        } else {
            binding.prbMain.visibility = View.INVISIBLE
        }
    }

    private fun initViewPager(fragments: List<Fragment>, fragmentNames: List<String>) {
        val listTypeAdapter = MovieListTypePagerAdapter(this, fragments)
        tabLayout = binding.tlHome
        viewPager = binding.vpHome
        viewPager?.adapter = listTypeAdapter
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = fragmentNames[position]
        }.attach()
    }

    private fun toggleDisplayType() {
        if (homeViewModel.currentDisplayType.value == MovieItemDisplayType.GRID) {
            homeViewModel.setDisplayType(MovieItemDisplayType.VERTICAL_LINEAR)
        } else {
            homeViewModel.setDisplayType(MovieItemDisplayType.GRID)
        }
    }

}