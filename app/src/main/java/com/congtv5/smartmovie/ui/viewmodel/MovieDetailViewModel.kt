package com.congtv5.smartmovie.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.usecase.GetCastAndCrewListUseCase
import com.congtv5.domain.usecase.GetMovieDetailUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.MovieDetailViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getCastAndCrewListUseCase: GetCastAndCrewListUseCase
) : BaseViewModel<MovieDetailViewState>() {

    private var loadMovieDetail: Job? = null

    override fun initState(): MovieDetailViewState {
        return MovieDetailViewState(
            movieDetail = null,
            casts = listOf(),
            isMovieInfoLoading = false,
            isCastLoading = false,
            isError = false
        )
    }

    fun getMovieDetail(movieId: Int) {
        loadMovieDetail?.cancel()
        loadMovieDetail = viewModelScope.launch {
            setIsMovieInfoLoading(true)
            setIsCastLoading(true)
            setIsError(false)

            coroutineScope {
                when (val result = getMovieDetailUseCase.execute(movieId)) {
                    is Resource.Success -> {
                        store.dispatchState(newState = store.state.copy(movieDetail = result.data))
                    }
                    is Resource.Error -> {
                        setIsError(true)
                    }
                }
                setIsMovieInfoLoading(false)
            }

            coroutineScope {
                when (val result = getCastAndCrewListUseCase.execute(movieId)) {
                    is Resource.Success -> {
                        store.dispatchState(
                            newState = store.state.copy(
                                casts = result.data?.casts ?: listOf()
                            )
                        )
                    }
                    is Resource.Error -> {
                        setIsError(true)
                    }
                }
                setIsCastLoading(false)
            }

        }
    }

    private fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    private fun setIsMovieInfoLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isMovieInfoLoading = value))
    }

    private fun setIsCastLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isCastLoading = value))
    }

    override fun onCleared() {
        loadMovieDetail?.cancel()
        super.onCleared()
    }
}