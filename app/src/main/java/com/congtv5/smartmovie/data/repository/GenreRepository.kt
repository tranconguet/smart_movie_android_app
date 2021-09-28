package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.genre.Genres
import com.congtv5.smartmovie.data.remote.api.GenreApi
import com.congtv5.smartmovie.utils.Constants
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GenreRepository @Inject constructor(
    private var genreApi: GenreApi,
    private var dispatcherProvider: DispatcherProvider,
) : IGenreRepository {
    // get genre list
    override suspend fun getGenreList(): Resource<Genres> {
        return withContext(dispatcherProvider.io) {
            try {
                val genreList = genreApi.getGenreList()
                Resource.Success(genreList)
            } catch (e: Exception) {
                Resource.Error(Constants.NETWORK_ERROR_MESSAGE)
            }
        }
    }

}