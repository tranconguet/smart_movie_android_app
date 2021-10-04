package com.congtv5.smartmovie.ui.base.viewmodel

import androidx.lifecycle.*

class ViewStateStore<T>(
    initialState: T
) {
    private val stateLiveData = MutableLiveData(initialState)

    val state: T
        get() = stateLiveData.value!!

    fun <S> observeAnyway(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        stateLiveData.map(selector)
            .observe(owner, observer)
    }

    fun <S> observe(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        stateLiveData.map(selector)
            .distinctUntilChanged()
            .observe(owner, observer)
    }

    fun dispatchState(newState: T) {
        stateLiveData.value = newState
    }
}