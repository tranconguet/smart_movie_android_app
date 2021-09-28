package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.castandcrewlist.CastAndCrew
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.remote.api.MovieApi
import com.congtv5.smartmovie.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private var movieApi: MovieApi,
    private var dispatcherProvider: DispatcherProvider,
) : IMovieRepository {

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieDetail = movieApi.getMovieDetails(movieId)
                Resource.Success(movieDetail)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getCastAndCrewList(movieId: Int): Resource<CastAndCrew> {
        return withContext(dispatcherProvider.io) {
            try {
                val castAndCrew = movieApi.getCastAndCrewList(movieId)
                Resource.Success(castAndCrew)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getPopularMovieList(pageNumber: Int): Resource<MovieListPage>{
        delay(1000L) // for testing
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getPopularMovieList(pageNumber)
                Resource.Success(movieList)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getTopRatedMovieList(pageNumber: Int): Resource<MovieListPage>{
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getTopRatedMovieList(pageNumber)
                Resource.Success(movieList)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getUpComingMovieList(pageNumber: Int): Resource<MovieListPage>{
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getUpComingMovieList(pageNumber)
                Resource.Success(movieList)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getNowPlayingMovieList(pageNumber: Int): Resource<MovieListPage>{
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getNowPlayingMovieList(pageNumber)
                Resource.Success(movieList)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

}