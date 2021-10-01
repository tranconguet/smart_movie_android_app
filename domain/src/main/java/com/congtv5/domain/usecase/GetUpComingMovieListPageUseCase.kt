package com.congtv5.domain.usecase

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.repository.MovieRepository
import javax.inject.Inject

class GetUpComingMovieListPageUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(page: Int): Resource<MovieListPage> {
        return movieRepository.getUpComingMovieList(page)
    }
}