package com.congtv5.smartmovie.utils

import com.congtv5.smartmovie.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class StandardDispatchers : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val undefined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}