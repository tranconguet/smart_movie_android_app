package com.congtv5.smartmovie.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularListViewModel @Inject constructor(
    private var movieRepository: MovieRepository
) : ViewModel() {

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _isReloading = MutableStateFlow(false)
    val isReloading: StateFlow<Boolean> = _isReloading

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
        if (_currentPage.value >= 1)
            setIsLoadingMore(true)
        viewModelScope.launch {
            when (val moviesResource =
                movieRepository.getPopularMovieList(_currentPage.value + 1)) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        increasePage()
                        addToMovieListPages(newList)
                    }
                    setIsReloading(false)
                    if (_currentPage.value >= 1)
                        setIsLoadingMore(false)
                }
                else -> {
                    setIsReloading(false)
                    if (_currentPage.value >= 1)
                        setIsLoadingMore(false)
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

    fun clearData() {
        _currentPage.value = 0
        _popularMovieListPages.value = mutableListOf()
    }

    fun setIsError(value: Boolean) {
        _isError.value = value
    }

    fun setIsReloading(value: Boolean) {
        _isReloading.value = value
    }

    fun setCurrentPage(value: Int) {
        _currentPage.value = value
    }

}