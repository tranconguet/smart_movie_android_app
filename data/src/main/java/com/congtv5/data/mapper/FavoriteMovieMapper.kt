package com.congtv5.data.mapper

import com.congtv5.data.data.local.entity.FavoriteMovieEntity
import com.congtv5.domain.model.FavoriteMovie
import javax.inject.Inject

class FavoriteMovieMapper @Inject constructor() : Mapper<FavoriteMovieEntity, FavoriteMovie>() {

    override fun map(response: List<FavoriteMovieEntity>): List<FavoriteMovie> {
        return response.map { item -> map(item) }
    }

    override fun map(response: FavoriteMovieEntity): FavoriteMovie {
        return FavoriteMovie(
            movieId = response.movieId,
            isLiked = response.isLiked
        )
    }

    fun mapToResponse(response: FavoriteMovie): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            movieId = response.movieId,
            isLiked = response.isLiked
        )
    }

    fun mapToResponse(response: List<FavoriteMovie>): List<FavoriteMovieEntity> {
        return response.map { item -> mapToResponse(item) }
    }

}