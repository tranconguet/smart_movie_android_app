package com.congtv5.data.data.remote

import com.congtv5.data.response.castandcrewlist.CastAndCrewResponse
import com.congtv5.data.response.movie.MovieDetailResponse
import com.congtv5.data.response.pageresult.MovieListPageResponse
import com.congtv5.data.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("{movieId}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MovieDetailResponse

    @GET("{movieId}/credits?api_key=$API_KEY")
    suspend fun getCastAndCrewList(@Path("movieId") movieId: Int): CastAndCrewResponse

    @GET("popular?api_key=$API_KEY")
    suspend fun getPopularMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("upcoming?api_key=$API_KEY")
    suspend fun getUpComingMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("now_playing?api_key=$API_KEY")
    suspend fun getNowPlayingMovieList(@Query("page") page: Int): MovieListPageResponse

    @GET("movie?api_key=$API_KEY")
    suspend fun getSearchResultList(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): MovieListPageResponse

}