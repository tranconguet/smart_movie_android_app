package com.congtv5.data.repository

import com.congtv5.data.data.local.FavoriteMovieDao
import com.congtv5.data.mapper.FavoriteMovieMapper
import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.repository.FavoriteMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteMovieRepositoryImpl(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val dispatcherProvider: DispatcherProvider,
    private val favoriteMovieMapper: FavoriteMovieMapper,
) : FavoriteMovieRepository {

    override suspend fun getFavoriteMovieList(): Flow<List<FavoriteMovie>> {
        return withContext(dispatcherProvider.io) {
            favoriteMovieDao.getFavoriteMovie().map { list ->
                favoriteMovieMapper.map(list)
            }
        }
    }

    override suspend fun insertFavoriteMovie(movie: FavoriteMovie) {
        withContext(dispatcherProvider.io) {
            favoriteMovieDao.insertMovie(favoriteMovieMapper.mapToResponse(movie))
        }
    }

}