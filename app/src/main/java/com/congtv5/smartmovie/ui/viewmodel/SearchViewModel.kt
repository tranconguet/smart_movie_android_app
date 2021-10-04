package com.congtv5.smartmovie.ui.viewmodel

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
            moviePages = mutableListOf()
        )
    }

    fun getGenreList() {
        loadMovieListResult?.cancel()
        loadMovieListResult = viewModelScope.launch {
            when (val genresResource = getGenreListUseCase.execute()) {
                is Resource.Success -> {
                    genresResource.data?.let { newList ->
                        store.dispatchState(newState = store.state.copy(genreList = newList))
                    }
                }
                else -> {
                    Log.e("CongTV5", "SearchViewModel #getGenreList error")
                }
            }
        }
    }

    fun getNextMovieListPage() {
        handleLoadingState(true)
        viewModelScope.launch {
            when (val moviesResource =
                getSearchMovieListPageUseCase.execute(
                    query = store.state.currentQuery,
                    page = store.state.currentPage + 1,
                )) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        increasePage()
                        addResultListPage(newList)
                    }
                    handleLoadingState(false)
                }
                is Resource.Error -> {
                    handleLoadingState(false)
                }
            }
        }
    }

    private fun addResultListPage(newList: MovieListPage) {
        store.state.moviePages.add(newList)
        store.dispatchState(newState = store.state.copy(moviePages = store.state.moviePages))
    }

    private fun increasePage() {
        store.dispatchState(newState = store.state.copy(currentPage = store.state.currentPage + 1))
    }

    fun setCurrentQuery(value: String) {
        store.dispatchState(newState = store.state.copy(currentQuery = value))
    }

    fun getGenreNameById(genreId: Int): String? {
        return store.state.genreList.find { genre -> genre.id == genreId }?.name
    }

    fun clearResult() {
        store.dispatchState(
            newState = store.state.copy(
                currentPage = 0,
                moviePages = mutableListOf()
            )
        )
    }

    private fun handleLoadingState(isLoading: Boolean) {
        if (isLoading) {
            if (store.state.moviePages.isNotEmpty()) {
                setIsLoadingMore(true) // load more
            } else {
                setIsLoading(true) // load first time
            }
        } else {
            if (store.state.isLoadingMore) {
                setIsLoadingMore(false)
            } else {
                setIsLoading(false)
            }
        }
    }

    private fun setIsLoadingMore(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoadingMore = value))
    }

    private fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

    override fun onCleared() {
        loadMovieListResult?.cancel()
        super.onCleared()
    }
}