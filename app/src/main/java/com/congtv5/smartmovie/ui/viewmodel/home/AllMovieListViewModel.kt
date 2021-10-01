package com.congtv5.smartmovie.ui.viewmodel.home

import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.AllMovieListViewState
import javax.inject.Inject

class AllMovieListViewModel @Inject constructor(
) : BaseViewModel<AllMovieListViewState>() {

    override fun initState(): AllMovieListViewState {
        return AllMovieListViewState(
            isReloading = false,
            isLoading = false,
            isError = false
        )
    }

    fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

    fun setIsReloading(value: Boolean){
        store.dispatchState(newState = store.state.copy(isReloading = value))
    }

}