package com.congtv5.domain.repository

import com.congtv5.domain.Resource
import com.congtv5.domain.model.Genre
import com.congtv5.domain.model.MovieListPage

interface GenreRepository {
    suspend fun getGenreList(): Resource<List<Genre>>
    suspend fun getMovieListByGenre(genreId: Int, pageNumber: Int): Resource<MovieListPage>
}