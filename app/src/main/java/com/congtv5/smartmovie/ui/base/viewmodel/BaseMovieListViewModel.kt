package com.congtv5.smartmovie.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.ui.viewstate.MovieListViewState
import kotlinx.coroutines.launch

abstract class BaseMovieListViewModel : BaseViewModel<MovieListViewState>() {

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

    fun addMovieListPage(movieListPage: MovieListPage) {
        val newList = mutableListOf<MovieListPage>()
        newList.addAll(store.state.movieListPages)
        newList.add(movieListPage)
        store.dispatchState(newState = store.state.copy(movieListPages = newList))
    }

    fun getNextMovieListPage() {
        setLoadingState(true)
        viewModelScope.launch {
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

    fun setLoadingState(value: Boolean){
        if (store.state.currentPage >= 1)
            setIsLoadingMore(value)
        else
            setIsLoading(value)
    }

    private fun increasePage() {
        store.dispatchState(newState = store.state.copy(currentPage = store.state.currentPage + 1))
    }

    fun setIsLoadingMore(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoadingMore = value))
    }

    fun clearData() {
        store.dispatchState(newState = store.state.copy(currentPage = 0, movieListPages = listOf()))
    }

    fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    fun setIsReloading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isReloading = value))
    }

    fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

    fun setCurrentPage(value: Int) {
        store.dispatchState(newState = store.state.copy(currentPage = value))
    }
}