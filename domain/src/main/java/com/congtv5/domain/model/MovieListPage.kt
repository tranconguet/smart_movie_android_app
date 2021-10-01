package com.congtv5.domain.model

data class MovieListPage(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)