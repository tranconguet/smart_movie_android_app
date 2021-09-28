package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

interface IFavoriteMovieRepository {
    fun getFavoriteMovieList(): Flow<List<FavoriteMovieEntity>>
    suspend fun getFavoriteMovie(movieId: Int): Boolean
    suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity)
    suspend fun updateFavoriteMovie(movie: FavoriteMovieEntity)
}