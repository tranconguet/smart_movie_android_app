package com.congtv5.smartmovie.ui.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.*
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.HomeViewState
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
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

    fun isMovieFavorite(movieId: Int): Boolean {
        val favoriteMovieIds =
            _favoriteList.filter { item -> item.isLiked }.map { item -> item.movieId }
        return favoriteMovieIds.contains(movieId)
    }

    fun applyFavoriteToAllMovie(favList: List<FavoriteMovie>) {
        val sectionMap = currentState.movieSectionMap
        sectionMap.map { entry ->
            if (entry.value is Resource.Success) {
                val currentList = entry.value?.data?.results
                currentList?.forEach { movie ->
                    val favMovieReference =
                        favList.find { favMovie -> favMovie.movieId == movie.id }
                    movie.isFavoriteMovie = favMovieReference != null && favMovieReference.isLiked
                }
            }
        }
        store.dispatchState(newState = currentState.copy(movieSectionMap = sectionMap))
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
            rsList.forEach { resource ->
                if (resource is Resource.Success) {
                    resource.data?.results?.forEach { movie ->
                        launch {
                            val movieDetail = getMovieDetailUseCase.execute(movie.id)
                            movie.runtime = movieDetail.data?.runtime ?: 0
                        }
                    }
                }
            }
            val sectionMap = mutableMapOf<MovieCategory, Resource<MovieListPage>?>(
                MovieCategory.POPULAR to rsList[0],
                MovieCategory.TOP_RATED to rsList[1],
                MovieCategory.UP_COMING to rsList[2],
                MovieCategory.NOW_PLAYING to rsList[3],
            )
            if (sectionMap.values.any { it is Resource.Success }){
                setIsError(false)
            }else{
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

    override fun onCleared() {
        loadMovieListJob?.cancel()
        loadFavoriteMovieJob?.cancel()
        insertFavoriteMovieJob?.cancel()
        super.onCleared()
    }

    private fun getEmptySectionMap(): MutableMap<MovieCategory, Resource<MovieListPage>?> {
        return mutableMapOf(
            MovieCategory.POPULAR to null,
            MovieCategory.TOP_RATED to null,
            MovieCategory.UP_COMING to null,
            MovieCategory.NOW_PLAYING to null
        )
    }
}