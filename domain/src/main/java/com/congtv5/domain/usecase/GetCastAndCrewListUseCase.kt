package com.congtv5.domain.usecase

import com.congtv5.domain.Resource
import com.congtv5.domain.model.CastAndCrew
import com.congtv5.domain.repository.MovieRepository
import javax.inject.Inject

class GetCastAndCrewListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(movieId: Int): Resource<CastAndCrew> {
        return movieRepository.getCastAndCrewList(movieId)
    }
}