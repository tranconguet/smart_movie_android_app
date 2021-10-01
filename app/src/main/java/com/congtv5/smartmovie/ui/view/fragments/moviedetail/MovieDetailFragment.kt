package com.congtv5.smartmovie.ui.view.fragments.moviedetail

import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.domain.model.MovieDetail
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.custom.ExpandableTextView
import com.congtv5.smartmovie.ui.view.adapter.CastListAdapter
import com.congtv5.smartmovie.ui.viewmodel.MovieDetailViewModel
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
    private var navController: NavController? = null

    private lateinit var ivBackButton: ImageView
    private lateinit var ivMovieImage: ImageView
    private lateinit var tvGenres: TextView
    private lateinit var tvLanguages: TextView
    private lateinit var tvMovieDescription: ExpandableTextView
    private lateinit var tvMovieName: TextView
    private lateinit var tvMovieTimeInfo: TextView
    private lateinit var tvRating: TextView
    private lateinit var rvCastList: RecyclerView
    private lateinit var rbRatingBar: RatingBar
    private lateinit var prbMovieInfo: ProgressBar
    private lateinit var prbCast: ProgressBar
    private lateinit var layoutMovieInfo: ConstraintLayout
    private lateinit var layoutCast: LinearLayout

    override fun getLayoutID(): Int {
        return R.layout.fragment_movie_detail
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        ivBackButton = view.findViewById(R.id.ivBackButton)
        ivMovieImage = view.findViewById(R.id.ivMovieImage)
        tvGenres = view.findViewById(R.id.tvGenres)
        tvLanguages = view.findViewById(R.id.tvLanguages)
        tvMovieDescription = view.findViewById(R.id.tvMovieDescription)
        tvMovieName = view.findViewById(R.id.tvMovieName)
        tvMovieTimeInfo = view.findViewById(R.id.tvMovieTimeInfo)
        tvRating = view.findViewById(R.id.tvRating)
        rvCastList = view.findViewById(R.id.rvCastList)
        rbRatingBar = view.findViewById(R.id.rbRatingBar)
        prbMovieInfo = view.findViewById(R.id.prbMovieInfo)
        prbCast = view.findViewById(R.id.prbCast)
        layoutMovieInfo = view.findViewById(R.id.layoutMovieInfo)
        layoutCast = view.findViewById(R.id.layoutCast)
    }


    override fun initObserveData() {

        movieDetailViewModel.store.observe(
            owner = this,
            selector = { state -> state.isMovieInfoLoading },
            observer = { isMovieInfoLoading ->
                handleMovieInfoLoading(isMovieInfoLoading)
            }
        )

        movieDetailViewModel.store.observe(
            owner = this,
            selector = { state -> state.isCastLoading },
            observer = { isCastLoading ->
                handleCastLoading(isCastLoading)
            }
        )

        movieDetailViewModel.store.observe(
            owner = this,
            selector = { state -> state.movieDetail },
            observer = { movieDetail ->
                Log.d("MovieDetailFragment", "#initObserveData movieDetail: $movieDetail")
                setView(movieDetail)
            }
        )

        movieDetailViewModel.store.observe(
            owner = this,
            selector = { state -> state.casts },
            observer = { castList ->
                Log.d("MovieDetailFragment", "#initObserveData castList: $castList")
                castListAdapter.submitList(castList)
            }
        )

    }

    private fun handleCastLoading(isLoading: Boolean) {
        if (isLoading) {
            prbCast.visibility = View.VISIBLE
            layoutCast.visibility = View.INVISIBLE
        } else {
            prbCast.visibility = View.INVISIBLE
            layoutCast.visibility = View.VISIBLE
        }
    }

    private fun handleMovieInfoLoading(isLoading: Boolean) {
        if (isLoading) {
            prbMovieInfo.visibility = View.VISIBLE
            layoutMovieInfo.visibility = View.INVISIBLE
        } else {
            prbMovieInfo.visibility = View.INVISIBLE
            layoutMovieInfo.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        val movieId = args.movieId
        movieDetailViewModel.getMovieDetail(movieId)
    }

    override fun initView() {
        navController = findNavController()
        initAdapter()
    }

    private fun initAdapter() {
        rvCastList.adapter = castListAdapter
        rvCastList.layoutManager =
            GridLayoutManager(context, CAST_ITEM_PER_COLUMN, RecyclerView.HORIZONTAL, false)
    }

    override fun initAction() {
        ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setView(movieDetail: MovieDetail?) {
        val glide = Glide.with(this)
        movieDetail?.let {
            val imageUrl = Constants.IMAGE_BASE_URL + movieDetail.posterPath
            glide.load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivMovieImage)
            tvMovieName.text = movieDetail.title
            tvMovieDescription.text = movieDetail.overview

            val productCountry = if (movieDetail.productionCountries.isNotEmpty()) {
                movieDetail.productionCountries[0].iso_3166_1
            } else EMPTY_TEXT
            tvMovieTimeInfo.text = formatMovieDurationToString(
                movieDetail.runtime,
                movieDetail.releaseDate,
                productCountry
            )

            val language = if (movieDetail.spokenLanguages.isNotEmpty()) {
                movieDetail.spokenLanguages[0].name
            } else {
                EMPTY_TEXT
            }
            tvLanguages.text = formatLanguageToString(language)

            tvGenres.text = formatGenresToString(movieDetail.genres)
            tvRating.text = formatRatingToString(movieDetail.voteAverage)

            rbRatingBar.numStars = movieDetail.voteAverage.toFloat().toInt() / 2
            rbRatingBar.rating = movieDetail.voteAverage.toFloat() / 2
        }
    }

}