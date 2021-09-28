package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.utils.Resource

interface ISearchRepository {
    suspend fun getSearchResultList(pageNumber: Int, query: String): Resource<MovieListPage>
}