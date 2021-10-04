package com.congtv5.data.data.remote

import com.congtv5.data.BuildConfig
import com.congtv5.data.response.genre.GenresResponse
import retrofit2.http.GET

interface GenreApi {

    @GET("movie/list?api_key=${BuildConfig.MOVIE_DB_ACCESS_KEY}")
    suspend fun getGenreList(): GenresResponse

}