package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.model.MovieListPage

data class MovieListViewState(
    val movieListPages: MutableList<MovieListPage>,
    val currentPage: Int,
    val isLoading: Boolean,
    val isLoadingMore: Boolean,
    val isError: Boolean
)