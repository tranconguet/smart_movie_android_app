package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.genre.Genres
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IGenreRepository {
    fun getGenreList(): Flow<Resource<Genres>>
}