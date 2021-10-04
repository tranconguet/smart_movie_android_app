package com.congtv5.data.data.remote

import com.congtv5.data.BuildConfig
import com.congtv5.data.response.pageresult.MovieListPageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("movie?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}&query=abc")
    suspend fun getSearchResultList(
        @Query("page") page: Int,
        @Query("query") query: String
    ): MovieListPageResponse

}