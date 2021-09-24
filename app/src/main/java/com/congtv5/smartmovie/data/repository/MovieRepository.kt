package com.congtv5.smartmovie.data.repository

import android.util.Log
import com.congtv5.smartmovie.data.model.castandcrewlist.CastAndCrew
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.remote.api.MovieApi
import com.congtv5.smartmovie.utils.Constants.API_KEY
import com.congtv5.smartmovie.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private var movieApi: MovieApi,
    private var dispatcherProvider: DispatcherProvider,
) : IMovieRepository {

    override fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetail>> = flow {
        val result: Resource<MovieDetail> = try {
            val movieListPage = movieApi.getMovieDetails(movieId)
            Resource.Success(movieListPage)
        } catch (e: Exception) {
            Resource.Error(NETWORK_ERROR_MESSAGE)
        }
        emit(result)
    }.flowOn(dispatcherProvider.io)

    override fun getCastAndCrewList(movieId: Int): Flow<Resource<CastAndCrew>> = flow {
        val result: Resource<CastAndCrew> = try {
            val movieListPage = movieApi.getCastAndCrewList(movieId)
            Resource.Success(movieListPage)
        } catch (e: Exception) {
            Resource.Error(NETWORK_ERROR_MESSAGE)
        }
        emit(result)
    }.flowOn(dispatcherProvider.io)

    override fun getPopularMovieList(pageNumber: Int): Flow<Resource<MovieListPage>> = flow {
        delay(2000L) // for testing
        val result: Resource<MovieListPage> = try {
            val movieListPage = movieApi.getPopularMovieList(pageNumber)
            Resource.Success(movieListPage)
        } catch (e: Exception) {
            Resource.Error(NETWORK_ERROR_MESSAGE)
        }
        emit(result)
    }.flowOn(dispatcherProvider.io)

    override fun getTopRatedMovieList(pageNumber: Int): Flow<Resource<MovieListPage>> = flow {
        val result: Resource<MovieListPage> = try {
            val movieListPage = movieApi.getTopRatedMovieList(pageNumber)
            Resource.Success(movieListPage)
        } catch (e: Exception) {
            Resource.Error(NETWORK_ERROR_MESSAGE)
        }
        emit(result)
    }.flowOn(dispatcherProvider.io)

    override fun getUpComingMovieList(pageNumber: Int): Flow<Resource<MovieListPage>> = flow {
        val result: Resource<MovieListPage> = try {
            val movieListPage = movieApi.getUpComingMovieList(pageNumber)
            Resource.Success(movieListPage)
        } catch (e: Exception) {
            Resource.Error(NETWORK_ERROR_MESSAGE)
        }
        emit(result)
    }.flowOn(dispatcherProvider.io)

    override fun getNowPlayingMovieList(pageNumber: Int): Flow<Resource<MovieListPage>> = flow {
        val result: Resource<MovieListPage> = try {
            val movieListPage = movieApi.getNowPlayingMovieList(pageNumber)
            Resource.Success(movieListPage)
        } catch (e: Exception) {
            Resource.Error(NETWORK_ERROR_MESSAGE)
        }
        emit(result)
    }.flowOn(dispatcherProvider.io)

}