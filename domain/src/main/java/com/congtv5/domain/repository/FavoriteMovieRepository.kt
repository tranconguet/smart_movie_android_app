package com.congtv5.domain.repository

import com.congtv5.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.Flow


interface FavoriteMovieRepository {
    suspend fun getFavoriteMovieList(): Flow<List<FavoriteMovie>>
    suspend fun getIsFavoriteMovie(movieId: Int): Boolean
    suspend fun insertFavoriteMovie(movie: FavoriteMovie)
    suspend fun updateFavoriteMovie(movie: FavoriteMovie)
}