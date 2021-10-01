package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.model.Genre

data class GenreListViewState(
    val genres: List<Genre>,
    val isError: Boolean,
    val isLoading: Boolean
)