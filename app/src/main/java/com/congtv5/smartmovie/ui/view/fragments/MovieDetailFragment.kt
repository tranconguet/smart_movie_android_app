package com.congtv5.smartmovie.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.databinding.FragmentMovieDetailBinding
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.viewmodel.MovieDetailViewModel
import com.congtv5.smartmovie.utils.*
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    override fun initObserveData() {
        lifecycleScope.launchWhenResumed {
            movieDetailViewModel.movieDetail.collect { movieDetail ->
                setView(movieDetail)
            }
        }
    }

    override fun initData() {
        val movieId = args.movieId
        movieDetailViewModel.getMovieDetail(movieId)
    }

    override fun initView() {
    }

    override fun initAction() {
    }


    private fun setView(movieDetail: MovieDetail?) {
        val glide = Glide.with(this)
        movieDetail?.let {
            val imageUrl = Constants.IMAGE_BASE_URL + movieDetail.poster_path
            glide.load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(binding.ivMovieImage)
            binding.tvMovieName.text = movieDetail.title
            binding.tvMovieDescription.text = movieDetail.overview

            val productCountry = if (movieDetail.production_countries.isNotEmpty()) {
                movieDetail.production_countries[0].iso_3166_1
            } else EMPTY_TEXT
            binding.tvMovieTimeInfo.text = formatMovieDurationToString(
                movieDetail.runtime,
                movieDetail.release_date,
                productCountry
            )

            val language = if(movieDetail.spoken_languages.isNotEmpty()){
                movieDetail.spoken_languages[0].name
            }else{
                EMPTY_TEXT
            }
            binding.tvLanguages.text = formatLanguageToString(language)

            binding.tvGenres.text = formatGenresToString(movieDetail.genres)
            binding.tvRating.text = formatRatingToString(movieDetail.vote_average)
        }
    }
}