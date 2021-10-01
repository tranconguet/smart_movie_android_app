package com.congtv5.domain.usecase

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchMovieListPageUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend fun execute(query: String, page: Int): Resource<MovieListPage> {
        return searchRepository.getSearchResultList(page, query)
    }
}