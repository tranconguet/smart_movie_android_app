package com.congtv5.smartmovie.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.model.movie.MovieDetail
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private var movieRepository: MovieRepository,
) : ViewModel() {
    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            setIsLoading(true)
            movieRepository.getMovieDetails(movieId).collect { movieResource ->
                when(movieResource){
                    is Resource.Success -> {
                        _movieDetail.value = movieResource.data
                    }
                    else -> {
                        setIsError(true)
                    }
                }
                setIsLoading(false)
            }
        }
    }

    fun setIsError(value: Boolean){
        _isError.value = value
    }

    fun setIsLoading(value: Boolean){
        _isLoading.value = value
    }
}