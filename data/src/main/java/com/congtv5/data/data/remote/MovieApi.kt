package com.congtv5.data.data.remote

import com.congtv5.data.BuildConfig
import com.congtv5.data.response.castandcrewlist.CastAndCrewResponse
import com.congtv5.data.response.movie.MovieDetailResponse
import com.congtv5.data.response.pageresult.MovieListPageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("{movieId}?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MovieDetailResponse

    @GET("{movieId}/credits?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getCastAndCrewList(@Path("movieId") movieId: Int): CastAndCrewResponse

    @GET("popular?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getPopularMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("top_rated?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getTopRatedMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("upcoming?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getUpComingMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("now_playing?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getNowPlayingMovieList(@Query("page") page: Int): MovieListPageResponse

}