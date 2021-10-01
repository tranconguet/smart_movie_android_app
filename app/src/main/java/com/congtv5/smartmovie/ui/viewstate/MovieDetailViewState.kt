package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.model.Cast
import com.congtv5.domain.model.MovieDetail

data class MovieDetailViewState(
    val movieDetail: MovieDetail?,
    val casts: List<Cast>,
    val isMovieInfoLoading: Boolean,
    val isCastLoading: Boolean,
    val isError: Boolean
)


//private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
//val movieDetail: StateFlow<MovieDetail?> = _movieDetail
//
//private val _casts = MutableStateFlow<List<Cast>>(listOf())
//val casts: StateFlow<List<Cast>> = _casts
//
//private val _isError = MutableStateFlow(false)
//val isError: StateFlow<Boolean> = _isError
//
//private val _isLoading = MutableStateFlow(false)
//val isLoading: StateFlow<Boolean> = _isLoading