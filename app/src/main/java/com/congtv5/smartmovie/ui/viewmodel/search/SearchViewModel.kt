package com.congtv5.smartmovie.ui.viewmodel.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetGenreListUseCase
import com.congtv5.domain.usecase.GetSearchMovieListPageUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.SearchViewState
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getSearchMovieListPageUseCase: GetSearchMovieListPageUseCase
) : BaseViewModel<SearchViewState>() {

    private var loadMovieListResult: Job? = null

    override fun initState(): SearchViewState {
        return SearchViewState(
            currentQuery = EMPTY_TEXT,
            genreList = listOf(),
            currentPage = 0,
            isLoading = false,
            isLoadingMore = false,
            isError = false,
            moviePages = mutableListOf()
        )
    }

    fun getGenreList() {
        loadMovieListResult?.cancel()
        loadMovieListResult = viewModelScope.launch {
            when (val genresResource = getGenreListUseCase.execute()) {
                is Resource.Success -> {
                    genresResource.data?.let { newList ->
                        store.dispatchState(newState = currentState.copy(genreList = newList))
                    }
                }
                else -> {
                    Log.e("CongTV5", "SearchViewModel #getGenreList error")
                }
            }
        }
    }

    fun getNextMovieListPage() {
        setIsError(false)
        handleLoadingState(true)
        viewModelScope.launch {
            when (val moviesResource =
                getSearchMovieListPageUseCase.execute(
                    query = currentState.currentQuery,
                    page = currentState.currentPage + 1,
                )) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        increasePage()
                        addResultListPage(newList)
                    }
                }
                is Resource.Error -> {
                    setIsError(true)
                }
            }
            handleLoadingState(false)
        }
    }

    private fun addResultListPage(newList: MovieListPage) {
        currentState.moviePages.add(newList)
        store.dispatchState(newState = currentState.copy(moviePages = currentState.moviePages))
    }

    private fun increasePage() {
        store.dispatchState(newState = currentState.copy(currentPage = currentState.currentPage + 1))
    }

    fun setCurrentQuery(value: String) {
        store.dispatchState(newState = currentState.copy(currentQuery = value))
    }

    fun getGenreNameById(genreId: Int): String? {
        return currentState.genreList.find { genre -> genre.id == genreId }?.name
    }

    private fun handleLoadingState(isLoading: Boolean) {
        if (isLoading) {
            if (currentState.moviePages.isNotEmpty()) {
                setIsLoadingMore(true) // load more
            } else {
                setIsLoading(true) // load first time
            }
        } else {
            if (currentState.isLoadingMore) {
                setIsLoadingMore(false)
            } else {
                setIsLoading(false)
            }
        }
    }

    private fun setIsLoadingMore(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isLoadingMore = value))
    }

    private fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isLoading = value))
    }

    private fun setIsError(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isError = value))
    }

    fun clearResult() {
        store.dispatchState(
            newState = currentState.copy(
                currentPage = 0,
                moviePages = mutableListOf()
            )
        )
    }

    override fun onCleared() {
        loadMovieListResult?.cancel()
        super.onCleared()
    }
}