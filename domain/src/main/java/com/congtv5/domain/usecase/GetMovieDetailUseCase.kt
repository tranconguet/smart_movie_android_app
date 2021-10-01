package com.congtv5.domain.usecase

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(movieId: Int): Resource<MovieDetail> {
        return movieRepository.getMovieDetails(movieId)
    }
}