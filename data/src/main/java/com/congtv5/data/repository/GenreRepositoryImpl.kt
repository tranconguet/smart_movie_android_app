package com.congtv5.data.repository

import com.congtv5.data.data.remote.DiscoverApi
import com.congtv5.data.data.remote.GenreApi
import com.congtv5.data.mapper.GenreMapper
import com.congtv5.data.mapper.MovieListPageMapper
import com.congtv5.data.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.domain.Resource
import com.congtv5.domain.model.Genre
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.repository.GenreRepository
import kotlinx.coroutines.withContext


class GenreRepositoryImpl(
    private val genreApi: GenreApi,
    private val discoverApi: DiscoverApi,
    private val dispatcherProvider: DispatcherProvider,
    private val genreMapper: GenreMapper,
    private val movieListPageMapper: MovieListPageMapper
) : GenreRepository {

    override suspend fun getGenreList(): Resource<List<Genre>> {
        return withContext(dispatcherProvider.io) {
            try {
                val genreList = genreApi.getGenreList()
                Resource.Success(genreMapper.map(genreList.genres ?: listOf()))
            } catch (e: Exception) { // catch specific exception
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getMovieListByGenre(
        genreId: Int,
        pageNumber: Int
    ): Resource<MovieListPage> {
        return withContext(dispatcherProvider.io) {
            try {
                val movieListPage = discoverApi.getMovieListByGenre(genreId, pageNumber)
                Resource.Success(movieListPageMapper.map(movieListPage))
            } catch (e: Exception) {
                Resource.Error(NETWORK_ERROR_MESSAGE)
            }
        }
    }

}