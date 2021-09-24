package com.congtv5.smartmovie.data.repository

import android.util.Log
import com.congtv5.smartmovie.data.model.genre.Genres
import com.congtv5.smartmovie.data.remote.api.GenreApi
import com.congtv5.smartmovie.utils.Constants
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject


class GenreRepository @Inject constructor(
    private var genreApi: GenreApi,
    private var dispatcherProvider: DispatcherProvider,
) : IGenreRepository {

    override fun getGenreList(): Flow<Resource<Genres>> = flow {

        val result: Resource<Genres> = try {
            val genreList = genreApi.getGenreList()
            Resource.Success(genreList)
        } catch (e: Exception) {
            Resource.Error(Constants.NETWORK_ERROR_MESSAGE)
        }
        emit(result)

    }.flowOn(dispatcherProvider.io)

}