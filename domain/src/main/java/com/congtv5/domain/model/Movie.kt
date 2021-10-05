package com.congtv5.domain.model

data class Movie(
    val backdropPath: String, // background image url
    val genreIds: List<Int>,
    val id: Int,
    val overview: String,
    val posterPath: String, // poster url
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    var isFavoriteMovie: Boolean = false,
    var runtime: Int
)