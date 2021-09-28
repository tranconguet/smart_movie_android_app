package com.congtv5.smartmovie.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AllMovieListViewModel @Inject constructor(
) : ViewModel() {

    private var _isReloading = MutableStateFlow(false)
    val isReloading: StateFlow<Boolean> = _isReloading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setIsError(value: Boolean) {
        _isError.value = value
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun setIsReloading(value: Boolean){
        _isReloading.value = value
    }
}