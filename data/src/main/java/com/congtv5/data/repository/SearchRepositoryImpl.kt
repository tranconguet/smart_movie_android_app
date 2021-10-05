package com.congtv5.data.repository

import com.congtv5.data.data.remote.SearchApi
import com.congtv5.data.mapper.MovieListPageMapper
import com.congtv5.data.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.repository.SearchRepository
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SearchRepositoryImpl(
    private var searchApi: SearchApi,
    private var dispatcherProvider: DispatcherProvider,
    private val movieListPageMapper: MovieListPageMapper
) : SearchRepository {

    override suspend fun getSearchResultList(
        pageNumber: Int,
        query: String
    ): Resource<MovieListPage> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieList = searchApi.getSearchResultList(pageNumber, query)
                Resource.Success(movieListPageMapper.map(movieList))
            } catch (e: IOException) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            } catch (e: HttpException) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            } catch (e: SocketTimeoutException) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

}