package com.congtv5.smartmovie.ui.view.fragments.home

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.congtv5.domain.Resource
import com.congtv5.domain.model.Movie
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.view.adapter.MovieListTypePagerAdapter
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.utils.Constants.MOVIES_TEXT
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.getFragmentByMovieCategory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    companion object {
        const val GRID_ITEM_PER_ROW = 2
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    private var successLoadingCategories = mutableListOf<MovieCategory>()

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var ivDisplayType: ImageView
    private lateinit var tvReload: TextView
    private lateinit var layoutHome: LinearLayout
    private lateinit var errorNotification: LinearLayout
    private lateinit var prbMain: ProgressBar

    override fun getLayoutID(): Int {
        return R.layout.fragment_home
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        tabLayout = view.findViewById(R.id.tlHome)
        viewPager = view.findViewById(R.id.vpHome)
        ivDisplayType = view.findViewById(R.id.ivDisplayType)
        tvReload = view.findViewById(R.id.tvReload)
        layoutHome = view.findViewById(R.id.layoutHome)
        errorNotification = view.findViewById(R.id.errorNotification)
        prbMain = view.findViewById(R.id.prbMain)
    }

    override fun initObserveData() {

        homeViewModel.store.observe(
            owner = this,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                setUpProgressBar(isLoading)
            }
        )

        homeViewModel.store.observe(
            owner = this,
            selector = { state -> state.isError },
            observer = { isError ->
                setUpError(isError)
            }
        )

        homeViewModel.store.observe(
            owner = this,
            selector = { state -> state.movieSectionMap },
            observer = { movieSectionMap ->
                updateLoadingProgressBarBySectionMap(movieSectionMap)
                updateIsErrorBySectionMap(movieSectionMap)
                updateMovieSectionTab(movieSectionMap)
            }
        )

        homeViewModel.store.observe(
            owner = this,
            selector = { state -> state.currentDisplayType },
            observer = { type ->
                when (type!!) {
                    MovieItemDisplayType.GRID -> {
                        ivDisplayType.setImageResource(R.drawable.linear_display)
                    }
                    MovieItemDisplayType.VERTICAL_LINEAR -> {
                        ivDisplayType.setImageResource(R.drawable.grid_display)
                    }
                }
            }
        )

        homeViewModel.store.observe(
            owner = this,
            selector = { state -> state.currentPageType },
            observer = { movieCategory ->
                if (movieCategory == null) {
                    viewPager.currentItem = 0 // navigate to allMovies page
                } else {
                    val pageIndex = successLoadingCategories.indexOf(movieCategory)
                    if (pageIndex >= 0) {
                        viewPager.currentItem = pageIndex + 1
                    }
                }
            }
        )

    }

    override fun initData() {
        homeViewModel.getFavoriteList()
    }

    override fun initView() {
    }

    override fun initAction() {
        tvReload.setOnClickListener {
            reloadInitData()
        }

        ivDisplayType.setOnClickListener {
            toggleDisplayType()
        }
    }

    private fun updateLoadingProgressBarBySectionMap(allMovieSections: Map<MovieCategory, Resource<List<Movie>>?>) {
        // for loading
        val isLoadingDone = allMovieSections.filter { item ->
            item.value == null
        }.isEmpty()
        if (isLoadingDone) homeViewModel.setIsLoading(false)
    }

    private fun updateIsErrorBySectionMap(allMovieSections: Map<MovieCategory, Resource<List<Movie>>?>) {
        // for loading
        val isLoadingDone = allMovieSections.filter { item ->
            item.value == null
        }.isEmpty()
        val isError = allMovieSections.filter { item ->
            item.value is Resource.Success
        }.isEmpty()
        if (isLoadingDone && isError) homeViewModel.setIsError(true)
    }

    private fun updateMovieSectionTab(allMovieSections: Map<MovieCategory, Resource<List<Movie>>?>) {
        // prepare data to show
        // movies tab is default
        val fragments = mutableListOf<Fragment>(AllMovieFragment())
        val fragmentNames = mutableListOf(MOVIES_TEXT)
        val movieCategories = mutableListOf<MovieCategory>()

        val successLoadingCategory = allMovieSections.filter { item ->
            item.value is Resource.Success
        }
        successLoadingCategory.keys.forEach { movieCategory ->
            fragments.add(getFragmentByMovieCategory(movieCategory))
            movieCategories.add(movieCategory)
        }

        successLoadingCategories = movieCategories

        successLoadingCategory.keys.forEach { movieCategory ->
            fragmentNames.add(movieCategory.text)
        }

        if (fragments.size > 1 && !homeViewModel.store.state.isLoading) { //at least 1 page call success
            initViewPager(fragments, fragmentNames)
            showAllPage()
        }

    }

    private fun showAllPage() {
        layoutHome.visibility = View.VISIBLE
    }

    private fun reloadInitData() {
        if (homeViewModel.store.state.isError) {
            homeViewModel.setIsError(false)
            loadInitData()
        }
    }

    private fun loadInitData() {
        homeViewModel.setIsLoading(true)
        successLoadingCategories.clear()
        homeViewModel.clearAllData()
        homeViewModel.getMovieListToInitAllPage()
    }

    private fun setUpError(isError: Boolean) {
        if (isError) {
            errorNotification.visibility = View.VISIBLE
        } else {
            errorNotification.visibility = View.INVISIBLE
        }
    }

    private fun setUpProgressBar(isLoading: Boolean) {
        if (isLoading) {
            prbMain.visibility = View.VISIBLE
        } else {
            prbMain.visibility = View.INVISIBLE
        }
    }

    private fun initViewPager(fragments: List<Fragment>, fragmentNames: List<String>) {
        val listTypeAdapter = MovieListTypePagerAdapter(this, fragments)

        viewPager.adapter = listTypeAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    homeViewModel.setCurrentPageType(null)
                } else {
                    val currentPage = successLoadingCategories[position - 1]
                    homeViewModel.setCurrentPageType(currentPage)
                }
            }
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentNames[position]
        }.attach()
    }

    private fun toggleDisplayType() {
        if (homeViewModel.store.state.currentDisplayType == MovieItemDisplayType.GRID) {
            homeViewModel.setDisplayType(MovieItemDisplayType.VERTICAL_LINEAR)
        } else {
            homeViewModel.setDisplayType(MovieItemDisplayType.GRID)
        }
    }

}