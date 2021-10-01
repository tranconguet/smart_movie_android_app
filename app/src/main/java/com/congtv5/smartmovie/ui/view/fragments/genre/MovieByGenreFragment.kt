package com.congtv5.smartmovie.ui.view.fragments.genre

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.MovieListFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.viewmodel.genre.MovieByGenreViewModel
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import javax.inject.Inject

class MovieByGenreFragment : MovieListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val myHomeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    @Inject
    lateinit var movieByGenreViewModel: MovieByGenreViewModel

    private val args: MovieByGenreFragmentArgs by navArgs()
    lateinit var tvPageTitle: TextView
    lateinit var ivBackButton: ImageView
    lateinit var ivDisplayType: ImageView

    override fun getLayoutID(): Int {
        return R.layout.fragment_movie_by_genre
    }

    override fun initBinding(view: View) {
        super.initBinding(view)
        tvPageTitle = view.findViewById(R.id.tvPageTitle)
        ivBackButton = view.findViewById(R.id.ivBackButton)
        ivDisplayType = view.findViewById(R.id.ivDisplayType)
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initViewModel() {
        Log.d("CongTV5", "MovieByGenreFragment #initViewModel ${args.genreId} ${args.genreTitle}")
        homeViewModel = myHomeViewModel
        movieListViewModel = movieByGenreViewModel
    }

    override fun initObserveData() {
        super.initObserveData()

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

    }

    override fun initView() {
        super.initView()
        tvPageTitle.text = args.genreTitle
    }

    override fun initAction() {
        super.initAction()
        ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        ivDisplayType.setOnClickListener {
            toggleDisplayType()
        }
    }

    override fun initData() {
        super.initData()
        movieByGenreViewModel.setGenreId(args.genreId)
        movieByGenreViewModel.getNextMovieListPage()
        homeViewModel.setIsLoading(false) // for change display type
    }

    override fun getFirstMovieListPage(): MovieListPage? {
        return null
    }

    private fun toggleDisplayType() {
        Log.d(
            "CongTV5",
            "MovieByGenreFragment #toggleDisplayType ${homeViewModel.store.state.currentDisplayType}"
        )
        if (homeViewModel.store.state.currentDisplayType == MovieItemDisplayType.GRID) {
            homeViewModel.setDisplayType(MovieItemDisplayType.VERTICAL_LINEAR)
        } else {
            homeViewModel.setDisplayType(MovieItemDisplayType.GRID)
        }
    }

    override fun goToMovieDetailPage(movieId: Int) {
        val action = MovieByGenreFragmentDirections.actionMovieByGenreFragmentToMovieDetailFragment3(movieId)
        findNavController().navigate(action)
    }

}