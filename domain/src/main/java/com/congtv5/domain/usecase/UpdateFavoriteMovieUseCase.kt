package com.congtv5.domain.usecase

import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

class UpdateFavoriteMovieUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) {
    suspend fun execute(favoriteMovie: FavoriteMovie){
        return favoriteMovieRepository.updateFavoriteMovie(favoriteMovie)
    }
}