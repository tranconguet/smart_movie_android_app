package com.congtv5.data.mapper

import com.congtv5.data.response.pageresult.MovieListPageResponse
import com.congtv5.data.response.pageresult.MovieResponse
import com.congtv5.data.utils.Constants.EMPTY_TEXT
import com.congtv5.domain.model.Movie
import com.congtv5.domain.model.MovieListPage
import javax.inject.Inject

class MovieListPageMapper @Inject constructor(
    private val movieMapper: MovieMapper
): Mapper<MovieListPageResponse, MovieListPage>() {

    override fun map(response: List<MovieListPageResponse>): List<MovieListPage> {
        return response.map { item -> map(item) }
    }

    override fun map(response: MovieListPageResponse): MovieListPage {
        return MovieListPage(
            page = response.page ?: 0,
            totalPages = response.totalPages ?: 0,
            totalResults = response.totalResults ?: 0,
            results = response.results?.let { movieMapper.map(it) } ?: listOf()
        )
    }

}

class MovieMapper @Inject constructor() : Mapper<MovieResponse, Movie>() {

    override fun map(response: List<MovieResponse>): List<Movie> {
        return response.map { item -> map(item) }
    }

    override fun map(response: MovieResponse): Movie {
        return Movie(
            id = response.id ?: 0,
            backdropPath = response.backdropPath ?: EMPTY_TEXT,
            genreIds = response.genreIds ?: listOf(),
            overview = response.overview ?: EMPTY_TEXT,
            posterPath = response.posterPath ?: EMPTY_TEXT,
            releaseDate = response.releaseDate ?: EMPTY_TEXT,
            title = response.title ?: EMPTY_TEXT,
            voteAverage = response.voteAverage ?: 0.0,
            runtime = 0 // default
        )
    }

}