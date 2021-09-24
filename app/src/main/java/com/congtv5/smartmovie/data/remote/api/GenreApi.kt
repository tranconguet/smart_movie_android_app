package com.congtv5.smartmovie.data.remote.api

import com.congtv5.smartmovie.data.model.genre.Genres
import com.congtv5.smartmovie.utils.Constants.API_KEY
import retrofit2.http.GET

interface GenreApi {

    @GET("movie/list?api_key=$API_KEY")
    suspend fun getGenreList(): Genres

}