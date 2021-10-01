package com.congtv5.data.response.pageresult

import com.google.gson.annotations.SerializedName

data class MovieListPageResponse(
    @field:SerializedName("page") val page: Int?,
    @field:SerializedName("results") val results: List<MovieResponse>?,
    @field:SerializedName("totalPages") val totalPages: Int?,
    @field:SerializedName("totalResults") val totalResults: Int?
)

data class MovieResponse(
    @field:SerializedName("adult") val adult: Boolean?,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("genre_ids") val genreIds: List<Int>?,
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("original_language") val originalLanguage: String?,
    @field:SerializedName("original_title") val originalTitle: String?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("popularity") val popularity: Double?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("video") val video: Boolean?,
    @field:SerializedName("vote_average") val voteAverage: Double?,
    @field:SerializedName("vote_count") val voteCount: Int?
)