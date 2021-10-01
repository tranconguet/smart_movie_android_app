package com.congtv5.data.data.remote

import com.congtv5.data.response.pageresult.MovieListPageResponse
import com.congtv5.data.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {

    @GET("movie?api_key=${API_KEY}")
    suspend fun getMovieListByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): MovieListPageResponse

}