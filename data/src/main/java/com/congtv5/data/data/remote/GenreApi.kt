package com.congtv5.data.data.remote

import com.congtv5.data.response.genre.GenresResponse
import com.congtv5.data.utils.Constants.API_KEY
import retrofit2.http.GET

interface GenreApi {

    @GET("movie/list?api_key=$API_KEY")
    suspend fun getGenreList(): GenresResponse

}