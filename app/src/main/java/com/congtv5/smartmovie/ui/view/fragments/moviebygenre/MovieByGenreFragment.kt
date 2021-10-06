package com.congtv5.smartmovie.ui.view.fragments.moviebygenre

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseMovieListFragment
import com.congtv5.smartmovie.ui.base.viewmodel.ViewModelFactory
import com.congtv5.smartmovie.ui.viewmodel.home.HomeViewModel
import com.congtv5.smartmovie.ui.viewmodel.moviebygenre.MovieByGenreViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import javax.inject.Inject

class MovieByGenreFragment : BaseMovieListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val myHomeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    @Inject
    lateinit var movieByGenreViewModel: MovieByGenreViewModel

    private val args: MovieByGenreFragmentArgs by navArgs()
    private lateinit var pageTitleTextView: TextView
    private lateinit var backButtonImageView: ImageView
    private lateinit var displayTypeImageView: ImageView

    override fun getLayoutID(): Int {
        return R.layout.fragment_movie_by_genre
    }

    override fun initBinding(view: View) {
        super.initBinding(view)
        pageTitleTextView = view.findViewById(R.id.tvPageTitle)
        backButtonImageView = view.findViewById(R.id.ivBackButton)
        displayTypeImageView = view.findViewById(R.id.ivDisplayType)
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initViewModel() {
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
                        displayTypeImageView.setImageResource(R.drawable.linear_display)
                    }
                    MovieItemDisplayType.VERTICAL_LINEAR -> {
                        displayTypeImageView.setImageResource(R.drawable.grid_display)
                    }
                }
            }
        )

    }

    override fun initView() {
        super.initView()
        pageTitleTextView.text = args.genreTitle
    }

    override fun initAction() {
        super.initAction()
        backButtonImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        displayTypeImageView.setOnClickListener {
            toggleDisplayType()
        }
    }

    override fun initData() {
        super.initData()
        if (movieListViewModel.store.state.currentPage == 0) {
            movieByGenreViewModel.setGenreId(args.genreId)
            movieByGenreViewModel.getNextMovieListPage()
        }
    }

    override fun getFirstMovieListPage(): MovieListPage? {
        // this movieList wasn't called when begin so call first page in initData()
        return null
    }

    private fun toggleDisplayType() {
        if (homeViewModel.store.state.currentDisplayType == MovieItemDisplayType.GRID) {
            homeViewModel.setDisplayType(
                MovieItemDisplayType.VERTICAL_LINEAR,
                movieByGenreViewModel.store.state.isLoading
            )
        } else {
            homeViewModel.setDisplayType(
                MovieItemDisplayType.GRID,
                movieByGenreViewModel.store.state.isLoading
            )
        }
    }

    override fun goToMovieDetailPage(movieId: Int) {
        val action =
            MovieByGenreFragmentDirections.actionMovieByGenreFragmentToMovieDetailFragment3(movieId)
        findNavController().navigate(action)
    }

}