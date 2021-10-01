package com.congtv5.domain.usecase

import com.congtv5.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

class GetIsFavoriteMovieListUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) {
    suspend fun execute(movieId: Int): Boolean {
        return favoriteMovieRepository.getIsFavoriteMovie(movieId)
    }
}