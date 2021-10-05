package com.congtv5.smartmovie.ui.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<S : Any> : ViewModel() {

    val store by lazy {
        ViewStateStore(this.initState())
    }
    val currentState: S
        get() = store.state

    abstract fun initState(): S

    override fun onCleared() {
        Log.d("test", "onCleared")
        super.onCleared()
    }
}
