package com.congtv5.domain.model

data class MovieDetail(
    val genres: List<Genre>,
    val id: Int,
    val overview: String,
    val posterPath: String,
    val productionCountries: List<ProductionCountry>,
    val releaseDate: String,
    val runtime: Int,
    val spokenLanguages: List<SpokenLanguage>,
    val title: String,
    val voteAverage: Double
)