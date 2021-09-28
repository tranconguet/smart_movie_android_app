package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.castandcrewlist.CastAndCrew
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.utils.Resource

interface IMovieRepository {
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail>
    suspend fun getCastAndCrewList(movieId: Int): Resource<CastAndCrew>
    suspend fun getPopularMovieList(pageNumber: Int): Resource<MovieListPage>
    suspend fun getTopRatedMovieList(pageNumber: Int): Resource<MovieListPage>
    suspend fun getUpComingMovieList(pageNumber: Int): Resource<MovieListPage>
    suspend fun getNowPlayingMovieList(pageNumber: Int):Resource<MovieListPage>
}