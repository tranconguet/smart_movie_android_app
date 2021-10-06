package com.congtv5.smartmovie.ui.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.*
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.HomeViewState
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import kotlinx.coroutines.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getPopularMovieListPageUseCase: GetPopularMovieListPageUseCase,
    private val getTopRatedMovieListPageUseCase: GetTopRatedMovieListPageUseCase,
    private val getUpComingMovieListPageUseCase: GetUpComingMovieListPageUseCase,
    private val getNowPlayingMovieListPageUseCase: GetNowPlayingMovieListPageUseCase,
    private val getFavoriteMovieListUseCase: GetFavoriteMovieListUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : BaseViewModel<HomeViewState>() {

    private var loadFavoriteMovieJob: Job? = null
    private var insertFavoriteMovieJob: Job? = null
    private var loadMovieListJob: Job? = null

    private var _favoriteList = listOf<FavoriteMovie>()

    override fun initState(): HomeViewState {
        return HomeViewState(
            isLoading = false,
            isError = false,
            currentDisplayType = MovieItemDisplayType.GRID,
            currentPageType = null,
            favoriteList = null,
            movieSectionMap = getEmptySectionMap()
        )
    }

    init {
        getFavoriteList()
        getMovieListToInitAllPage()
    }

    private fun getFavoriteList() {
        loadFavoriteMovieJob?.cancel()
        loadFavoriteMovieJob = viewModelScope.launch {
            val favList = getFavoriteMovieListUseCase.execute()
            store.dispatchState(newState = currentState.copy(favoriteList = favList))
        }
    }

    fun updateFavoriteMovie(favoriteMovie: FavoriteMovie) {
        insertFavoriteMovieJob?.cancel()
        insertFavoriteMovieJob = viewModelScope.launch {
            insertFavoriteMovieUseCase.execute(favoriteMovie)
        }
    }

    fun applyFavoriteToAllMovie(favList: List<FavoriteMovie>) {
        currentState.movieSectionMap.map { entry ->
            if (entry.value is Resource.Success) {
                val currentList = entry.value?.data?.results
                currentList?.forEach { movie ->
                    val favMovieReference =
                        favList.find { favMovie -> favMovie.movieId == movie.id }
                    movie.isFavoriteMovie = favMovieReference != null && favMovieReference.isLiked
                }
            }
        }
        store.dispatchState(newState = currentState.copy(movieSectionMap = currentState.movieSectionMap))
    }

    fun getMovieListToInitAllPage() {
        setIsError(false)
        setIsLoading(true)
        clearAllData()
        loadMovieListJob?.cancel()
        loadMovieListJob = viewModelScope.launch {
            val popularUseCase = async {
                getPopularMovieListPageUseCase.execute(1)
            }
            val topRatedUseCase = async {
                getTopRatedMovieListPageUseCase.execute(1)
            }
            val upComingUseCase = async {
                getUpComingMovieListPageUseCase.execute(1)
            }
            val nowPlayingUseCase = async {
                getNowPlayingMovieListPageUseCase.execute(1)
            }
            val rsList = listOf(
                popularUseCase,
                topRatedUseCase,
                upComingUseCase,
                nowPlayingUseCase
            ).awaitAll()
            val movieList = mutableListOf<Movie>()
            rsList.forEach { resource ->
                movieList.addAll(resource.data?.results ?: listOf())
            }
            movieList.map { movie ->
                launch { // get each movie runtime in parallel because api doesn't provide movie runtime
                    val movieDetail = getMovieDetailUseCase.execute(movie.id)
                    movie.runtime = movieDetail.data?.runtime ?: 0
                }
            }.joinAll()
            val sectionMap = mutableMapOf<MovieCategory, Resource<MovieListPage>?>(
                MovieCategory.POPULAR to rsList[0],
                MovieCategory.TOP_RATED to rsList[1],
                MovieCategory.UP_COMING to rsList[2],
                MovieCategory.NOW_PLAYING to rsList[3],
            )
            if (sectionMap.values.any { it is Resource.Success }) {
                setIsError(false)
            } else {
                setIsError(true)
            }
            store.dispatchState(newState = currentState.copy(movieSectionMap = sectionMap))
            setIsLoading(false)
        }
    }

    fun setDisplayType(value: MovieItemDisplayType, isLoading: Boolean) {
        // requirement: not allow toggle when loading
        if (!isLoading)
            store.dispatchState(newState = currentState.copy(currentDisplayType = value))
    }

    private fun clearAllData() {
        store.dispatchState(
            newState = currentState.copy(
                movieSectionMap = mutableMapOf(
                    MovieCategory.POPULAR to null,
                    MovieCategory.TOP_RATED to null,
                    MovieCategory.UP_COMING to null,
                    MovieCategory.NOW_PLAYING to null
                )
            )
        )
    }

    fun setFavoriteList(favoriteList: List<FavoriteMovie>) {
        _favoriteList = favoriteList
    }

    private fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isLoading = value))
    }

    private fun setIsError(value: Boolean) {
        store.dispatchState(newState = currentState.copy(isError = value))
    }

    fun setCurrentPageType(movieCategory: MovieCategory?) {
        store.dispatchState(newState = currentState.copy(currentPageType = movieCategory))
    }

    private fun getEmptySectionMap(): MutableMap<MovieCategory, Resource<MovieListPage>?> {
        return mutableMapOf(
            MovieCategory.POPULAR to null,
            MovieCategory.TOP_RATED to null,
            MovieCategory.UP_COMING to null,
            MovieCategory.NOW_PLAYING to null
        )
    }

    override fun onCleared() {
        loadMovieListJob?.cancel()
        loadFavoriteMovieJob?.cancel()
        insertFavoriteMovieJob?.cancel()
        super.onCleared()
    }
}