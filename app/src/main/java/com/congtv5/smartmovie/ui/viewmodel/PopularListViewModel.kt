package com.congtv5.smartmovie.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularListViewModel @Inject constructor(
    private var movieRepository: MovieRepository
) : ViewModel() {

    private var _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private var _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var _popularMovieListPages = MutableStateFlow(mutableListOf<MovieListPage>())
    val popularMovieListPages: StateFlow<List<MovieListPage>> = _popularMovieListPages

    fun addMovieListPage(movieListPage: MovieListPage) {
        _popularMovieListPages.value.add(movieListPage)
    }

    fun getNextMovieListPage() {
        setIsLoadingMore(true)
        viewModelScope.launch {
            movieRepository.getPopularMovieList(_currentPage.value + 1)
                .collect { movieListPagesResource ->
                    delay(3000L)
                    when (movieListPagesResource) {
                        is Resource.Success -> {
                            movieListPagesResource.data?.let { newList ->
                                increasePage()
                                addToMovieListPages(newList)
                            }
                            setIsLoadingMore(false)
                        }
                        else -> {
                            setIsLoadingMore(false)
                        }
                    }
                }
        }
    }

    private fun addToMovieListPages(newList: MovieListPage) {
        val list = mutableListOf<MovieListPage>()
        list.addAll(_popularMovieListPages.value)
        list.add(newList)
        _popularMovieListPages.value = list
    }

    private fun increasePage() {
        _currentPage.value = _currentPage.value + 1
    }

    fun setIsLoadingMore(value: Boolean) {
        _isLoadingMore.value = value
    }

}