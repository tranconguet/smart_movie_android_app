package com.congtv5.smartmovie.data.remote.api

import com.congtv5.smartmovie.data.model.castandcrewlist.CastAndCrew
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("{movieId}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MovieDetail

    @GET("{movieId}/credits?api_key=$API_KEY")
    suspend fun getCastAndCrewList(@Path("movieId") movieId: Int): CastAndCrew

    @GET("popular?api_key=$API_KEY")
    suspend fun getPopularMovieList(@Query("page") page: Int): MovieListPage

    @GET("top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovieList(@Query("page") page: Int): MovieListPage

    @GET("upcoming?api_key=$API_KEY")
    suspend fun getUpComingMovieList(@Query("page") page: Int): MovieListPage

    @GET("now_playing?api_key=$API_KEY")
    suspend fun getNowPlayingMovieList(@Query("page") page: Int): MovieListPage

    @GET("movie?api_key=$API_KEY")
    suspend fun getSearchResultList(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): MovieListPage

}