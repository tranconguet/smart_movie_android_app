package com.congtv5.smartmovie.ui.view.fragments

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.custom.ExpandableTextView
import com.congtv5.smartmovie.ui.view.adapter.CastListAdapter
import com.congtv5.smartmovie.ui.viewmodel.MovieDetailViewModel
import com.congtv5.smartmovie.utils.*
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment() {

    companion object {
        const val CAST_ITEM_PER_COLUMN = 2
    }

    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
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

    override fun getLayoutID(): Int {
        return R.layout.fragment_movie_detail
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
    }


    override fun initObserveData() {
        lifecycleScope.launchWhenResumed {
            coroutineScope {
                movieDetailViewModel.movieDetail.collect { movieDetail ->
                    setView(movieDetail)
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            coroutineScope {
                movieDetailViewModel.casts.collect { castList ->
                    castListAdapter.submitList(castList)
                }
            }
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
            val imageUrl = Constants.IMAGE_BASE_URL + movieDetail.poster_path
            glide.load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivMovieImage)
            tvMovieName.text = movieDetail.title
            tvMovieDescription.text = movieDetail.overview

            val productCountry = if (movieDetail.production_countries.isNotEmpty()) {
                movieDetail.production_countries[0].iso_3166_1
            } else EMPTY_TEXT
            tvMovieTimeInfo.text = formatMovieDurationToString(
                movieDetail.runtime,
                movieDetail.release_date,
                productCountry
            )

            val language = if (movieDetail.spoken_languages.isNotEmpty()) {
                movieDetail.spoken_languages[0].name
            } else {
                EMPTY_TEXT
            }
            tvLanguages.text = formatLanguageToString(language)

            tvGenres.text = formatGenresToString(movieDetail.genres)
            tvRating.text = formatRatingToString(movieDetail.vote_average)

            rbRatingBar.numStars = movieDetail.vote_average.toFloat().toInt() / 2
            rbRatingBar.rating = movieDetail.vote_average.toFloat() / 2
        }
    }

}