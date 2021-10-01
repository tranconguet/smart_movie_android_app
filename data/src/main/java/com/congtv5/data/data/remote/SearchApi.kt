package com.congtv5.data.data.remote

import com.congtv5.data.response.pageresult.MovieListPageResponse
import com.congtv5.data.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("movie?api_key=${API_KEY}&query=abc")
    suspend fun getSearchResultList(
        @Query("page") page: Int,
        @Query("query") query: String
    ): MovieListPageResponse

}