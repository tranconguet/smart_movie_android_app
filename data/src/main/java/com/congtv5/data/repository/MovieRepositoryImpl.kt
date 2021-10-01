package com.congtv5.data.repository

import android.util.Log
import com.congtv5.data.data.remote.MovieApi
import com.congtv5.data.mapper.CastAndCrewMapper
import com.congtv5.data.mapper.MovieDetailMapper
import com.congtv5.data.mapper.MovieListPageMapper
import com.congtv5.data.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.domain.Resource
import com.congtv5.domain.model.CastAndCrew
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.repository.MovieRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val dispatcherProvider: DispatcherProvider,
    private val movieDetailMapper: MovieDetailMapper,
    private val movieListPageMapper: MovieListPageMapper,
    private val castAndCrewMapper: CastAndCrewMapper
) : MovieRepository {

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieDetail = movieApi.getMovieDetails(movieId)
                Resource.Success(movieDetailMapper.map(movieDetail))
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getCastAndCrewList(movieId: Int): Resource<CastAndCrew> {
        return withContext(dispatcherProvider.io) {
            try {
                val castAndCrew = movieApi.getCastAndCrewList(movieId)
                val data = castAndCrewMapper.map(
                    castAndCrew
                )
                Log.d("CongTV5", "MovieRepositoryImpl #getCastAndCrewList castAndCrew $data")
                Resource.Success(castAndCrewMapper.map(castAndCrew))
            } catch (e: Exception) {
                Log.d("CongTV5", "MovieRepositoryImpl #getCastAndCrewList  Error $e")
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getPopularMovieList(pageNumber: Int): Resource<MovieListPage> {
        delay(1000L) // for testing
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getPopularMovieList(pageNumber)
                Resource.Success(movieListPageMapper.map(movieList))
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getTopRatedMovieList(pageNumber: Int): Resource<MovieListPage> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getTopRatedMovieList(pageNumber)
                Resource.Success(movieListPageMapper.map(movieList))
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getUpComingMovieList(pageNumber: Int): Resource<MovieListPage> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getUpComingMovieList(pageNumber)
                Resource.Success(movieListPageMapper.map(movieList))
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getNowPlayingMovieList(pageNumber: Int): Resource<MovieListPage> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = movieApi.getNowPlayingMovieList(pageNumber)
                Resource.Success(movieListPageMapper.map(movieList))
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

}