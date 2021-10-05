package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.model.Genre
import com.congtv5.domain.model.MovieListPage

data class SearchViewState(
    val currentQuery: String,
    val genreList: List<Genre>,
    val currentPage: Int,
    val isLoadingMore: Boolean,
    val isLoading: Boolean,
    val isError: Boolean,
    val moviePages: MutableList<MovieListPage>
)