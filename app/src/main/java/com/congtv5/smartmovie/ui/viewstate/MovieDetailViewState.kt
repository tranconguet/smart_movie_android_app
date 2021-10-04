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