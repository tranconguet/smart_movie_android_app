package com.congtv5.data.data.remote

import com.congtv5.data.BuildConfig
import com.congtv5.data.response.pageresult.MovieListPageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {

    @GET("movie?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getMovieListByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): MovieListPageResponse

}