package com.congtv5.data.repository

import com.congtv5.data.data.local.FavoriteMovieDao
import com.congtv5.data.mapper.FavoriteMovieMapper
import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.repository.FavoriteMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoriteMovieRepositoryImpl(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val dispatcherProvider: DispatcherProvider,
    private val favoriteMovieMapper: FavoriteMovieMapper,
) : FavoriteMovieRepository {

    override fun getFavoriteMovieList(): Flow<List<FavoriteMovie>> = flow {
        favoriteMovieDao.getFavoriteMovie().collect { list ->
            emit(favoriteMovieMapper.map(list))
        }
    }.flowOn(dispatcherProvider.io)

    override suspend fun getIsFavoriteMovie(movieId: Int): Boolean {
        return withContext(dispatcherProvider.io) {
            favoriteMovieDao.getFavoriteMovie(movieId).isLiked
        }
    }

    override suspend fun insertFavoriteMovie(movie: FavoriteMovie) {
        withContext(dispatcherProvider.io) {
            favoriteMovieDao.insertMovie(favoriteMovieMapper.mapToResponse(movie))
        }
    }

    override suspend fun updateFavoriteMovie(movie: FavoriteMovie) {
        withContext(dispatcherProvider.io) {
            favoriteMovieDao.updateFavoriteMovie(movieId = movie.movieId, value = movie.isLiked)
        }
    }

}