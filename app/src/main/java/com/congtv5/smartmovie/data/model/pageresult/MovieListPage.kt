package com.congtv5.smartmovie.data.model.pageresult

data class MovieListPage(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)