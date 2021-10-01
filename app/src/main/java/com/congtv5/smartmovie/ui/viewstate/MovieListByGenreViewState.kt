package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.model.MovieListPage

data class MovieListByGenreViewState(
    val genreId: Int,
    val movieListPages: List<MovieListPage>,
    val currentPage: Int,
    val isLoading: Boolean,
    val isLoadingMore: Boolean,
    val isError: Boolean,
    val isReloading: Boolean
)