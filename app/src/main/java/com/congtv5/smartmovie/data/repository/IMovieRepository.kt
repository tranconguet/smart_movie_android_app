package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.castandcrewlist.CastAndCrew
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

    fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetail>>
    fun getCastAndCrewList(movieId: Int): Flow<Resource<CastAndCrew>>
    fun getPopularMovieList(pageNumber: Int): Flow<Resource<MovieListPage>>
    fun getTopRatedMovieList(pageNumber: Int): Flow<Resource<MovieListPage>>
    fun getUpComingMovieList(pageNumber: Int): Flow<Resource<MovieListPage>>
    fun getNowPlayingMovieList(pageNumber: Int): Flow<Resource<MovieListPage>>

}