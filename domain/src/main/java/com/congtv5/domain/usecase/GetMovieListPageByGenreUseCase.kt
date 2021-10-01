package com.congtv5.domain.usecase

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.repository.GenreRepository
import javax.inject.Inject

class GetMovieListPageByGenreUseCase @Inject constructor(
    private val genreRepository: GenreRepository
) {
    suspend fun execute(genreId: Int, page: Int): Resource<MovieListPage> {
        return genreRepository.getMovieListByGenre(genreId, page)
    }
}