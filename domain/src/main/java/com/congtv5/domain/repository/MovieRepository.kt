package com.congtv5.domain.repository

import com.congtv5.domain.Resource
import com.congtv5.domain.model.CastAndCrew
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.MovieListPage

interface MovieRepository {
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail>
    suspend fun getCastAndCrewList(movieId: Int): Resource<CastAndCrew>
    suspend fun getPopularMovieList(pageNumber: Int): Resource<MovieListPage>
    suspend fun getTopRatedMovieList(pageNumber: Int): Resource<MovieListPage>
    suspend fun getUpComingMovieList(pageNumber: Int): Resource<MovieListPage>
    suspend fun getNowPlayingMovieList(pageNumber: Int):Resource<MovieListPage>
}