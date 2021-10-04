package com.congtv5.smartmovie.ui.view.fragments.genre

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
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
    lateinit var prbLoading: ProgressBar

    override fun getLayoutID(): Int {
        return R.layout.fragment_movie_by_genre
    }

    override fun initBinding(view: View) {
        super.initBinding(view)
        tvPageTitle = view.findViewById(R.id.tvPageTitle)
        ivBackButton = view.findViewById(R.id.ivBackButton)
        ivDisplayType = view.findViewById(R.id.ivDisplayType)
        prbLoading = view.findViewById(R.id.prbLoading)
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

        movieByGenreViewModel.store.observe(
            owner = this,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                handleIsLoading(isLoading)
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
        if (movieListViewModel.store.state.currentPage == 0) {
            Log.d("CongTV5", "MovieByGenreFragment #initData() initData")
            movieByGenreViewModel.setGenreId(args.genreId)
            movieByGenreViewModel.getNextMovieListPage()
        }
    }

    private fun handleIsLoading(isLoading: Boolean) {
        if (isLoading) {
            prbLoading.visibility = View.VISIBLE
        } else {
            prbLoading.visibility = View.INVISIBLE
        }
    }

    override fun getFirstMovieListPage(): MovieListPage? {
        //this movieList wasn't called when begin so call first page in initData()
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