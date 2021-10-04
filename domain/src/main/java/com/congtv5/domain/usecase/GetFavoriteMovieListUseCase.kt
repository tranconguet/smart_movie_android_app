package com.congtv5.domain.usecase

import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.repository.FavoriteMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMovieListUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) {
    suspend fun execute(): Flow<List<FavoriteMovie>> {
        return favoriteMovieRepository.getFavoriteMovieList()
    }
}