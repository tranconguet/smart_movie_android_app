package com.congtv5.smartmovie.data.remote.api

import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("movie?api_key=${API_KEY}&query=abc")
    suspend fun getSearchResultList(
        @Query("page") page: Int,
        @Query("query") query: String
    ): MovieListPage

}