package com.congtv5.smartmovie.data.repository

import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.remote.api.SearchApi
import com.congtv5.smartmovie.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.Resource
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private var searchApi: SearchApi,
    private var dispatcherProvider: DispatcherProvider,
) : ISearchRepository {

    override suspend fun getSearchResultList(
        pageNumber: Int,
        query: String
    ): Resource<MovieListPage> {
        return withContext(dispatcherProvider.io){
            try {
                val movieList = searchApi.getSearchResultList(pageNumber, query)
                Resource.Success(movieList)
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

}