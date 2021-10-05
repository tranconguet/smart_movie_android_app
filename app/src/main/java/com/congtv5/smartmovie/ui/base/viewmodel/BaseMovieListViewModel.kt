package com.congtv5.smartmovie.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.ui.viewstate.MovieListViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

abstract class BaseMovieListViewModel : BaseViewModel<MovieListViewState>() {

    private var loadMovieListJob: Job? = null

    abstract suspend fun getMovieListPage(page: Int): Resource<MovieListPage>
    abstract suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail>

    override fun initState(): MovieListViewState {
        return MovieListViewState(
            movieListPages = mutableListOf(),
            currentPage = 0,
            isLoading = false,
            isError = false,
            isLoadingMore = false,
        )
    }

    // apply favorite list from db to movie list from network
    fun applyFavoriteToAllMovie(favList: List<FavoriteMovie>) {
        currentState.movieListPages.forEach { movieListPage ->
            movieListPage.results.forEach { movie ->
                val favMovieReference = favList.find { favMovie -> favMovie.movieId == movie.id }
                movie.isFavoriteMovie = favMovieReference != null && favMovieReference.isLiked
            }
        }
        store.dispatchState(newState = currentState.copy(movieListPages = currentState.movieListPages))
    }

    fun addMovieListPage(movieListPage: MovieListPage) {
        currentState.movieListPages.add(movieListPage)
        store.dispatchState(newState = currentState.copy(movieListPages = currentState.movieListPages))
    }

    fun getNextMovieListPage() {
        setIsError(false)
        setLoadingState(true)
        loadMovieListJob?.cancel()
        loadMovieListJob = viewModelScope.launch {
            when (val moviesResource = getMovieListPage(currentState.currentPage + 1)) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        newList.results.map { movie ->
                            launch { // get each movie detail in parallel to get movie runtime
                                val movieDetail = getMovieDetail(movie.id)
                                movie.runtime = movieDetail.data?.runtime ?: 0
                            }
                        }.joinAll()
                        increasePage()
                        addMovieListPage(newList)
                    }
                    setLoadingState(false)
                }
                is Resource.Error -> {
                    setIsError(true)
                    setLoadingState(false)
                }
            }
        }
    }

    private fun setLoadingState(value: Boolean) {
        if (value) { // turn on
            if (currentState.currentPage >= 1)
                setIsLoadingMore(value)
            else
                setIsLoading(value)
        } else { // turn off
            if (currentState.currentPage > 1)
                setIsLoadingMore(value)
            else
                setIsLoading(value)
        }
    }

    fun clearData() {
        store.dispatchState(
            newState = currentState.copy(
                currentPage = 0,
                movieListPages = mutableListOf()
            )
        )
    }

    fun setCurrentPage(value: Int) {
        store.dispatchState(newState = currentState.copy(currentPage = value))
    }

    private fun increasePage() {
        store.dispatchState(newState = currentState.copy(currentPage = currentState.currentPage + 1))
    }

    private fun setIsLoadingMore(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isLoadingMore = value))
    }

    private fun setIsError(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isError = value))
    }

    private fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isLoading = value))
    }

    override fun onCleared() {
        loadMovieListJob?.cancel()
        super.onCleared()
    }
}