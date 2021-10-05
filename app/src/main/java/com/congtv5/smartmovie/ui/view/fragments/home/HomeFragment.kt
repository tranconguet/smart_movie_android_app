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
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.view.adapter.MovieListTypePagerAdapter
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.utils.Constants.MOVIES_TEXT
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
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
        errorNotification = view.findViewById(R.id.layoutError)
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
            selector = { state -> state.movieSectionMap },
            observer = { movieSectionMap ->
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
                    // navigate to other tab
                    val pageIndex = successLoadingCategories.indexOf(movieCategory)
                    if (pageIndex >= 0) {
                        viewPager.currentItem = pageIndex + 1 // include allMovie Tab
                    }
                }
            }
        )

    }

    override fun initData() {
        loadInitData()
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

    private fun updateMovieSectionTab(allMovieSections: Map<MovieCategory, Resource<MovieListPage>?>) {
        // prepare data to show
        val fragments = mutableListOf<Fragment>(AllMovieFragment())
        val fragmentNames = mutableListOf(MOVIES_TEXT) // movies tab is default
        val movieCategories = mutableListOf<MovieCategory>()

        allMovieSections.filter { item ->
            item.value is Resource.Success
        }.keys.forEach { movieCategory ->
            fragments.add(getFragmentByMovieCategory(movieCategory))
            movieCategories.add(movieCategory)
            fragmentNames.add(movieCategory.text)
        }

        successLoadingCategories = movieCategories
        if (fragments.size > 1) { //at least 1 page call success
            initViewPager(fragments, fragmentNames)
        }
    }


    private fun reloadInitData() {
        loadInitData()
    }

    private fun loadInitData() {
        successLoadingCategories.clear()
        homeViewModel.getMovieListToInitAllPage()
    }

    private fun setUpProgressBar(isLoading: Boolean) {
        if (isLoading) {
            prbMain.visibility = View.VISIBLE
            errorNotification.visibility = View.INVISIBLE
            layoutHome.visibility = View.INVISIBLE
        } else if (!isLoading && homeViewModel.currentState.isError) {
            errorNotification.visibility = View.VISIBLE
            prbMain.visibility = View.INVISIBLE
            layoutHome.visibility = View.INVISIBLE
        } else {
            prbMain.visibility = View.INVISIBLE
            errorNotification.visibility = View.INVISIBLE
            layoutHome.visibility = View.VISIBLE
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
                    if (successLoadingCategories.size > position) {
                        val currentPage = successLoadingCategories[position - 1]
                        homeViewModel.setCurrentPageType(currentPage)
                    }
                }
            }
        })
        viewPager.offscreenPageLimit = fragments.size // make fragment not be destroyed automatically
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentNames[position]
        }.attach()
    }

    private fun toggleDisplayType() {
        if (homeViewModel.store.state.currentDisplayType == MovieItemDisplayType.GRID) {
            homeViewModel.setDisplayType(
                MovieItemDisplayType.VERTICAL_LINEAR,
                homeViewModel.store.state.isLoading
            )
        } else {
            homeViewModel.setDisplayType(
                MovieItemDisplayType.GRID,
                homeViewModel.store.state.isLoading
            )
        }
    }

    private fun getFragmentByMovieCategory(movieCategory: MovieCategory): Fragment {
        return when (movieCategory) {
            MovieCategory.POPULAR -> PopularBaseMovieFragment()
            MovieCategory.TOP_RATED -> TopRatedBaseMovieFragment()
            MovieCategory.UP_COMING -> UpComingBaseMovieFragment()
            MovieCategory.NOW_PLAYING -> NowPlayingBaseMovieFragment()
        }
    }

}