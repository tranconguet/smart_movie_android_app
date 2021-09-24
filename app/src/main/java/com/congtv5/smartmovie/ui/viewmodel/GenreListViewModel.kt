package com.congtv5.smartmovie.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.model.genre.Genre
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.repository.GenreRepository
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreListViewModel @Inject constructor(
    private var genreRepository: GenreRepository,
) : ViewModel() {

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _genres = MutableStateFlow(listOf<Genre>())
    var genres: StateFlow<List<Genre>> = _genres

    fun getGenreList() {
        setIsLoading(true)
        viewModelScope.launch {
            genreRepository.getGenreList().collect { genreListResource ->
                when (genreListResource) {
                    is Resource.Success -> {
                        _genres.value = genreListResource.data?.genres ?: listOf()
                    }
                    else -> {
                        setIsError(true)
                    }
                }
                setIsLoading(false)
            }
        }
    }


    fun setIsError(value: Boolean) {
        _isError.value = value
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }
}