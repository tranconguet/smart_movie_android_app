package com.congtv5.domain.usecase

import com.congtv5.domain.Resource
import com.congtv5.domain.model.Genre
import com.congtv5.domain.repository.GenreRepository
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(
    private val genreRepository: GenreRepository
) {
    suspend fun execute(): Resource<List<Genre>> {
        return genreRepository.getGenreList()
    }
}