package com.congtv5.smartmovie.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.ui.viewstate.MovieListViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseMovieListViewModel : BaseViewModel<MovieListViewState>() {

    private var loadMovieListJob: Job? = null

    abstract suspend fun getMovieListPage(page: Int): Resource<MovieListPage>

    override fun initState(): MovieListViewState {
        return MovieListViewState(
            movieListPages = listOf(),
            currentPage = 0,
            isLoading = false,
            isError = false,
            isLoadingMore = false,
            isReloading = false
        )
    }

    // apply favorite list from db to movie list from network
    fun applyFavoriteToAllMovie(favList: List<FavoriteMovie>) {
        store.state.movieListPages.forEach { movieListPage ->
            movieListPage.results.forEach { movie ->
                val favMovieReference = favList.find { favMovie -> favMovie.movieId == movie.id }
                movie.isFavoriteMovie = favMovieReference != null && favMovieReference.isLiked
            }
        }
        store.dispatchState(newState = store.state.copy(movieListPages = store.state.movieListPages))
    }

    fun addMovieListPage(movieListPage: MovieListPage) {
        val newList = mutableListOf<MovieListPage>()
        newList.addAll(store.state.movieListPages)
        newList.add(movieListPage)
        store.dispatchState(newState = store.state.copy(movieListPages = newList))
    }

    fun getNextMovieListPage() {
        setLoadingState(true)
        loadMovieListJob?.cancel()
        loadMovieListJob = viewModelScope.launch {
            when (val moviesResource = getMovieListPage(store.state.currentPage + 1)) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        increasePage()
                        addMovieListPage(newList)
                    }
                    setIsReloading(false)
                    setLoadingState(false)
                }
                else -> {
                    setIsReloading(false)
                    setLoadingState(false)
                }
            }
        }
    }

    private fun setLoadingState(value: Boolean) {
        if (value) { // turn on
            if (store.state.currentPage >= 1)
                setIsLoadingMore(value)
            else
                setIsLoading(value)
        } else { // turn off
            if (store.state.currentPage > 1)
                setIsLoadingMore(value)
            else
                setIsLoading(value)
        }
    }

    fun clearData() {
        store.dispatchState(newState = store.state.copy(currentPage = 0, movieListPages = listOf()))
    }

    fun setCurrentPage(value: Int) {
        store.dispatchState(newState = store.state.copy(currentPage = value))
    }

    private fun increasePage() {
        store.dispatchState(newState = store.state.copy(currentPage = store.state.currentPage + 1))
    }

    private fun setIsLoadingMore(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoadingMore = value))
    }

    private fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    private fun setIsReloading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isReloading = value))
    }

    private fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

    override fun onCleared() {
        loadMovieListJob?.cancel()
        super.onCleared()
    }
}