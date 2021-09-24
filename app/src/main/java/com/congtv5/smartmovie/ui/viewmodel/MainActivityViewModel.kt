package com.congtv5.smartmovie.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.PageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivityViewModel : ViewModel() {
    private var _currentPage = MutableStateFlow(PageType.DISCOVER)
    val currentPage: StateFlow<PageType> = _currentPage

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }
}