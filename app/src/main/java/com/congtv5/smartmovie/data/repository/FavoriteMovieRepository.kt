package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.database.FavoriteMovieDao
import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import com.congtv5.smartmovie.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoriteMovieRepository(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val dispatcherProvider: DispatcherProvider
) : IFavoriteMovieRepository {

    override fun getFavoriteMovieList(): Flow<List<FavoriteMovieEntity>> = flow {
        favoriteMovieDao.getFavoriteMovie().collect { list ->
            emit(list)
        }
    }.flowOn(dispatcherProvider.io)

    override suspend fun getFavoriteMovie(movieId: Int): Boolean {
        return withContext(dispatcherProvider.io) {
            favoriteMovieDao.getFavoriteMovie(movieId).isLiked
        }
    }

    override suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity) {
        withContext(dispatcherProvider.io) {
            favoriteMovieDao.insertMovie(movie)
        }
    }

    override suspend fun updateFavoriteMovie(movie: FavoriteMovieEntity) {
        withContext(dispatcherProvider.io) {
            favoriteMovieDao.updateFavoriteMovie(movieId = movie.movieId, value = movie.isLiked)
        }
    }

}