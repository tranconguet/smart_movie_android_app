package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.genre.Genres
import com.congtv5.smartmovie.utils.Resource

interface IGenreRepository {
    suspend fun getGenreList(): Resource<Genres>
}