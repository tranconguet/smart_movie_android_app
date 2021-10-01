package com.congtv5.smartmovie.ui.viewmodel.genre

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.usecase.GetGenreListUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.GenreListViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class GenreListViewModel @Inject constructor(
    private var getGenreListUseCase: GetGenreListUseCase,
) : BaseViewModel<GenreListViewState>() {

    override fun initState(): GenreListViewState {
        return GenreListViewState(
            genres = listOf(),
            isLoading = false,
            isError = false
        )
    }

    fun getGenreList() {
        setIsLoading(true)
        viewModelScope.launch {
            when (val genresResource = getGenreListUseCase.execute()) {
                is Resource.Success -> {
                    store.dispatchState(
                        newState = store.state.copy(
                            genres = genresResource.data ?: listOf()
                        )
                    )
                }
                else -> {
                    setIsError(true)
                }
            }
            setIsLoading(false)
        }
    }


    fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }


}