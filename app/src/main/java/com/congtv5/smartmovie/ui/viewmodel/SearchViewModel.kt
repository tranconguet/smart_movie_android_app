package com.congtv5.smartmovie.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.model.movie.Genre
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.repository.GenreRepository
import com.congtv5.smartmovie.data.repository.SearchRepository
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private var searchRepository: SearchRepository,
    private var genreRepository: GenreRepository
) : ViewModel() {

    private var _currentQuery = MutableStateFlow(EMPTY_TEXT)
    val currentQuery: StateFlow<String> = _currentQuery

    private var _genreList = MutableStateFlow<List<Genre>>(listOf())
    val genreList: StateFlow<List<Genre>> = _genreList

    private var _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    private var _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _resultListPages = MutableStateFlow(mutableListOf<MovieListPage>())
    val resultListPages: StateFlow<List<MovieListPage>> = _resultListPages

    fun getGenreList() {
        viewModelScope.launch {
            when (val genresResource = genreRepository.getGenreList()) {
                is Resource.Success -> {
                    genresResource.data?.let { newList ->
                        _genreList.value = newList.genres
                    }
                }
                else -> {
                    Log.e("CongTV5", "SearchViewModel #getGenreList error when get genreList")
                }
            }
        }
    }

    fun getNextMovieListPage() {
        if (_resultListPages.value.isNotEmpty()) {
            setIsLoadingMore(true) // load more
        } else {
            setIsLoading(true) // load first time
        }
        viewModelScope.launch {
            when (val moviesResource =
                searchRepository.getSearchResultList(_currentPage.value + 1, currentQuery.value)) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        increasePage()
                        addResultListPage(newList)
                    }
                    if (_isLoadingMore.value) {
                        setIsLoadingMore(false)
                    } else {
                        setIsLoading(false)
                    }
                }
                else -> {
                    if (_isLoadingMore.value) {
                        setIsLoadingMore(false)
                    } else {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

    private fun addResultListPage(newList: MovieListPage) {
        val list = mutableListOf<MovieListPage>()
        list.addAll(_resultListPages.value)
        list.add(newList)
        _resultListPages.value = list
    }

    private fun increasePage() {
        _currentPage.value = _currentPage.value + 1
    }

    fun setCurrentQuery(value: String) {
        _currentQuery.value = value
    }

    fun getGenreNameById(genreId: Int): String? {
        return _genreList.value.find { genre -> genre.id == genreId }?.name
    }

    fun clearResult() {
        _currentPage.value = 0
        _resultListPages.value.clear()
    }

    fun setIsLoadingMore(value: Boolean) {
        _isLoadingMore.value = value
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

}