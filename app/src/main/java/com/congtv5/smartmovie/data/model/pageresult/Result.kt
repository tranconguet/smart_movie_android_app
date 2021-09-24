package com.congtv5.smartmovie.data.model.pageresult

data class Result(
    val adult: Boolean,
    val backdrop_path: String, // image url
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String, // poster url
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)