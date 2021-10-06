package com.congtv5.smartmovie.ui.view.fragments.moviedetail

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.domain.model.MovieDetail
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.CastListAdapter
import com.congtv5.smartmovie.ui.view.custom.ExpandableTextView
import com.congtv5.smartmovie.ui.viewmodel.moviedetail.MovieDetailViewModel
import com.congtv5.smartmovie.utils.*
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import javax.inject.Inject

class MovieDetailFragment : BaseFragment() {

    companion object {
        const val CAST_ITEM_PER_COLUMN = 2
    }

    @Inject
    lateinit var movieDetailViewModel: MovieDetailViewModel

    private val args: MovieDetailFragmentArgs by navArgs()
    private val castListAdapter = CastListAdapter()

    private lateinit var backButtonImageView: ImageView
    private lateinit var movieImageView: ImageView
    private lateinit var genresTextView: TextView
    private lateinit var languagesTextView: TextView
    private lateinit var movieDescriptionTextView: ExpandableTextView
    private lateinit var movieNameTextView: TextView
    private lateinit var movieTimeInfoTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var castListRecyclerView: RecyclerView
    private lateinit var ratingBar: RatingBar
    private lateinit var movieInfoLoadingProgressBar: ProgressBar
    private lateinit var castLoadingProgressBar: ProgressBar
    private lateinit var movieInfoLayout: ConstraintLayout
    private lateinit var castLayout: LinearLayout
    private lateinit var errorLayout: LinearLayout
    private lateinit var contentScrollView: ScrollView
    private lateinit var reloadTextView: TextView

    override fun getLayoutID(): Int {
        return R.layout.fragment_movie_detail
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        backButtonImageView = view.findViewById(R.id.ivBackButton)
        movieImageView = view.findViewById(R.id.ivMovieImage)
        genresTextView = view.findViewById(R.id.tvGenres)
        languagesTextView = view.findViewById(R.id.tvLanguages)
        movieDescriptionTextView = view.findViewById(R.id.tvMovieDescription)
        movieNameTextView = view.findViewById(R.id.tvMovieName)
        movieTimeInfoTextView = view.findViewById(R.id.tvMovieTimeInfo)
        ratingTextView = view.findViewById(R.id.tvRating)
        castListRecyclerView = view.findViewById(R.id.rvCastList)
        ratingBar = view.findViewById(R.id.rbRatingBar)
        movieInfoLoadingProgressBar = view.findViewById(R.id.prbMovieInfo)
        castLoadingProgressBar = view.findViewById(R.id.prbCast)
        movieInfoLayout = view.findViewById(R.id.layoutMovieInfo)
        castLayout = view.findViewById(R.id.layoutCast)
        errorLayout = view.findViewById(R.id.layoutError)
        contentScrollView = view.findViewById(R.id.svContent)
        reloadTextView = view.findViewById(R.id.tvReload)
    }

    override fun initObserveData() {

        movieDetailViewModel.store.observeDistinctValue(
            owner = this,
            selector = { state -> state.isMovieInfoLoading },
            observer = { isMovieInfoLoading ->
                handleMovieInfoLoading(isMovieInfoLoading)
            }
        )

        movieDetailViewModel.store.observeDistinctValue(
            owner = this,
            selector = { state -> state.isCastLoading },
            observer = { isCastLoading ->
                handleCastLoading(isCastLoading)
            }
        )

        movieDetailViewModel.store.observeDistinctValue(
            owner = this,
            selector = { state -> state.isError },
            observer = { isError ->
                handleError(isError)
            }
        )

        movieDetailViewModel.store.observeDistinctValue(
            owner = this,
            selector = { state -> state.movieDetail },
            observer = { movieDetail ->
                setMovieDetailView(movieDetail)
            }
        )

        movieDetailViewModel.store.observeDistinctValue(
            owner = this,
            selector = { state -> state.casts },
            observer = { castList ->
                castListAdapter.submitList(castList)
            }
        )

    }

    override fun initData() {
        val movieId = args.movieId
        movieDetailViewModel.getMovieDetail(movieId)
    }

    override fun initView() {
        initAdapter()
    }

    override fun initAction() {
        backButtonImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        reloadTextView.setOnClickListener {
            reloadData()
        }
    }

    private fun initAdapter() {
        castListRecyclerView.adapter = castListAdapter
        castListRecyclerView.layoutManager =
            GridLayoutManager(context, CAST_ITEM_PER_COLUMN, RecyclerView.HORIZONTAL, false)
    }

    private fun setMovieDetailView(movieDetail: MovieDetail?) {
        val glide = Glide.with(this)
        movieDetail?.let {

            val imageUrl = Constants.IMAGE_BASE_URL + movieDetail.posterPath
            glide.load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(movieImageView)

            movieNameTextView.text = movieDetail.title
            movieDescriptionTextView.text = movieDetail.overview

            val productCountry = if (movieDetail.productionCountries.isNotEmpty()) {
                movieDetail.productionCountries[0].iso_3166_1
            } else EMPTY_TEXT

            movieTimeInfoTextView.text = formatMovieDurationToString(
                movieDetail.runtime,
                movieDetail.releaseDate,
                productCountry
            )

            val language = if (movieDetail.spokenLanguages.isNotEmpty()) {
                movieDetail.spokenLanguages[0].name
            } else {
                EMPTY_TEXT
            }
            languagesTextView.text = language.formatLanguageToString()

            genresTextView.text = formatGenresToString(movieDetail.genres)
            ratingTextView.text = movieDetail.voteAverage.formatRatingToString()

            ratingBar.numStars = movieDetail.voteAverage.toFloat().toInt() / 2 // change range vote from 10 to 5
            ratingBar.rating = movieDetail.voteAverage.toFloat() / 2 // change range vote from 10 to 5
        }
    }

    private fun handleCastLoading(isLoading: Boolean) {
        if (isLoading) {
            castLoadingProgressBar.visibility = View.VISIBLE
            castLayout.visibility = View.INVISIBLE
        } else {
            castLoadingProgressBar.visibility = View.INVISIBLE
            castLayout.visibility = View.VISIBLE
        }
    }

    private fun handleMovieInfoLoading(isLoading: Boolean) {
        if (isLoading) {
            movieInfoLoadingProgressBar.visibility = View.VISIBLE
            movieInfoLayout.visibility = View.INVISIBLE
        } else {
            movieInfoLoadingProgressBar.visibility = View.INVISIBLE
            movieInfoLayout.visibility = View.VISIBLE
        }
    }

    private fun reloadData() {
        val movieId = args.movieId
        movieDetailViewModel.getMovieDetail(movieId)
    }

    private fun handleError(error: Boolean) {
        if(error){
            errorLayout.visibility = View.VISIBLE
            contentScrollView.visibility = View.INVISIBLE
        }else{
            errorLayout.visibility = View.INVISIBLE
            contentScrollView.visibility = View.VISIBLE
        }
    }

}