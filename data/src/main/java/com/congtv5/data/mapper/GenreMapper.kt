package com.congtv5.data.mapper

import com.congtv5.data.response.movie.GenreResponse
import com.congtv5.data.utils.Constants.EMPTY_TEXT
import com.congtv5.domain.model.Genre
import javax.inject.Inject

class GenreMapper @Inject constructor() : Mapper<GenreResponse, Genre>() {

    override fun map(response: GenreResponse): Genre {
        return Genre(
            id = response.id ?: 0,
            name = response.name ?: EMPTY_TEXT,
            backdropPath = null
        )
    }

    override fun map(response: List<GenreResponse>): List<Genre> {
        return response.map { item -> map(item) }
    }

}