package com.congtv5.domain.repository

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage

interface SearchRepository {
    suspend fun getSearchResultList(pageNumber: Int, query: String): Resource<MovieListPage>
}