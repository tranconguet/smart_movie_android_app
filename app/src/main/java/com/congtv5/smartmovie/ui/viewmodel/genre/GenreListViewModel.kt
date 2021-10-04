package com.congtv5.smartmovie.ui.viewmodel.genre

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.usecase.GetGenreListUseCase
import com.congtv5.domain.usecase.GetMovieListPageByGenreUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.GenreListViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class GenreListViewModel @Inject constructor(
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getMovieListPageByGenreUseCase: GetMovieListPageByGenreUseCase
) : BaseViewModel<GenreListViewState>() {

    private var loadGenreListJob: Job? = null

    override fun initState(): GenreListViewState {
        return GenreListViewState(
            genres = listOf(),
            isLoading = false,
            isError = false
        )
    }

    fun getGenreList() {
        setIsLoading(true)
        setIsError(false)
        loadGenreListJob?.cancel()
        loadGenreListJob = viewModelScope.launch {
            when (val genresResource = getGenreListUseCase.execute()) {
                is Resource.Success -> {
                    val genreList = genresResource.data ?: listOf()
                    genreList.forEach { genre ->
                        val firstPageOfGenre =
                            getMovieListPageByGenreUseCase.execute(genreId = genre.id, page = 1)
                        val movieListOfGenre = firstPageOfGenre.data?.results ?: listOf()
                        if (movieListOfGenre.isNotEmpty()) {
                            val randomIndex =
                                Random.nextInt(0, movieListOfGenre.size - 1)
                            genre.backdropPath = movieListOfGenre[randomIndex].backdropPath
                        }
                    }
                    store.dispatchState(newState = store.state.copy(genres = genreList))
                    Log.d("CongTV5", "GenreListViewModel #getGenreList() $genreList")
                }
                else -> {
                    setIsError(true)
                }
            }
            setIsLoading(false)
        }
    }


    private fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    private fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

    override fun onCleared() {
        loadGenreListJob?.cancel()
        super.onCleared()
    }

}