package com.congtv5.smartmovie.ui.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.domain.usecase.*
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.HomeViewState
import com.congtv5.smartmovie.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getPopularMovieListPageUseCase: GetPopularMovieListPageUseCase,
    private val getTopRatedMovieListPageUseCase: GetTopRatedMovieListPageUseCase,
    private val getUpComingMovieListPageUseCase: GetUpComingMovieListPageUseCase,
    private val getNowPlayingMovieListPageUseCase: GetNowPlayingMovieListPageUseCase,
    private val getFavoriteMovieListUseCase: GetFavoriteMovieListUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase
) : BaseViewModel<HomeViewState>() {

    private var loadFavoriteMovieJob: Job? = null
    private var insertFavoriteMovieJob: Job? = null
    private var loadMovieListJob: Job? = null

    companion object {
        const val ITEM_PER_SECTION = 4
    }

    private var _favoriteList = listOf<FavoriteMovie>()

    override fun initState(): HomeViewState {
        return HomeViewState(
            isLoading = false,
            isError = false,
            currentDisplayType = MovieItemDisplayType.GRID,
            currentPageType = null,
            favoriteList = null,
            movieSectionMap = mutableMapOf(),
            popularMovieFirstPage = null,
            topRatedMovieFirstPage = null,
            nowPlayingMovieFirstPage = null,
            upComingMovieFirstPage = null
        )
    }

    init {
        getFavoriteList()
    }

    private fun getFavoriteList() {
        loadFavoriteMovieJob?.cancel()
        loadFavoriteMovieJob = viewModelScope.launch {
            val favList = getFavoriteMovieListUseCase.execute()
            store.dispatchState(newState = store.state.copy(favoriteList = favList))
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
        val sectionMap = store.state.movieSectionMap
        sectionMap.map { entry ->
            if (entry.value is Resource.Success) {
                val currentList = (entry.value as? Resource.Success<List<Movie>>)?.data
                currentList?.forEach { movie ->
                    val favMovieReference =
                        favList.find { favMovie -> favMovie.movieId == movie.id }
                    movie.isFavoriteMovie = favMovieReference != null && favMovieReference.isLiked
                }
            }
        }
        store.dispatchState(newState = store.state.copy(movieSectionMap = sectionMap))
    }

    fun getMovieListToInitAllPage() {
        setIsLoading(true)
        clearAllData()
        loadMovieListJob?.cancel()
        loadMovieListJob = viewModelScope.launch { // combine coroutine
            coroutineScope {
                getPopularMovieListPageFirstLaunch()
            }
            coroutineScope {
                getTopRatedMovieListPageFirstLaunch()
            }
            coroutineScope {
                getUpComingMovieListPageFirstLaunch()
            }
            coroutineScope {
                getNowPlayingMovieListPageFirstLaunch()
            }
        }
    }

    private suspend fun getPopularMovieListPageFirstLaunch() {
        when (val moviesResource = getPopularMovieListPageUseCase.execute(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { moviePage ->
                    store.dispatchState(newState = store.state.copy(popularMovieFirstPage = moviePage))
                    val movieListForSection = if (moviePage.results.size >= ITEM_PER_SECTION) {
                        moviePage.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        moviePage.results
                    }
                    updateSectionMap(
                        MovieCategory.POPULAR,
                        Resource.Success(movieListForSection)
                    )
                }
            }
            else -> {
                updateSectionMap(
                    MovieCategory.POPULAR,
                    Resource.Error(NETWORK_ERROR_MESSAGE)
                )
            }
        }
    }

    private suspend fun getTopRatedMovieListPageFirstLaunch() {
        when (val moviesResource = getTopRatedMovieListPageUseCase.execute(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { moviePage ->
                    store.dispatchState(newState = store.state.copy(topRatedMovieFirstPage = moviePage))
                    val movieListForSection = if (moviePage.results.size >= ITEM_PER_SECTION) {
                        moviePage.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        moviePage.results
                    }
                    updateSectionMap(
                        MovieCategory.TOP_RATED,
                        Resource.Success(movieListForSection)
                    )
                }
            }
            else -> {
                updateSectionMap(
                    MovieCategory.TOP_RATED,
                    Resource.Error(NETWORK_ERROR_MESSAGE)
                )
            }
        }
    }

    private suspend fun getUpComingMovieListPageFirstLaunch() {
        when (val moviesResource = getUpComingMovieListPageUseCase.execute(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { moviePage ->
                    store.dispatchState(newState = store.state.copy(upComingMovieFirstPage = moviePage))
                    val movieListForSection = if (moviePage.results.size >= ITEM_PER_SECTION) {
                        moviePage.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        moviePage.results
                    }
                    updateSectionMap(
                        MovieCategory.UP_COMING,
                        Resource.Success(movieListForSection)
                    )
                }
            }
            else -> {
                updateSectionMap(
                    MovieCategory.UP_COMING,
                    Resource.Error(NETWORK_ERROR_MESSAGE)
                )
            }
        }
    }

    private suspend fun getNowPlayingMovieListPageFirstLaunch() {
        when (val moviesResource = getNowPlayingMovieListPageUseCase.execute(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { moviePage ->
                    store.dispatchState(newState = store.state.copy(nowPlayingMovieFirstPage = moviePage))
                    val movieListForSection = if (moviePage.results.size >= ITEM_PER_SECTION) {
                        moviePage.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        moviePage.results
                    }
                    updateSectionMap(
                        MovieCategory.NOW_PLAYING,
                        Resource.Success(movieListForSection)
                    )
                }
            }
            else -> {
                updateSectionMap(
                    MovieCategory.NOW_PLAYING,
                    Resource.Error(NETWORK_ERROR_MESSAGE)
                )
            }
        }
    }

    fun setDisplayType(value: MovieItemDisplayType, isLoading: Boolean) {
        // requirement: not allow toggle when loading
        if (!isLoading)
            store.dispatchState(newState = store.state.copy(currentDisplayType = value))
    }

    private fun updateSectionMap(
        sectionCategory: MovieCategory,
        resource: Resource<List<Movie>>?
    ) {
        val newMap = mutableMapOf<MovieCategory, Resource<List<Movie>>?>()
        newMap.putAll(store.state.movieSectionMap)
        newMap[sectionCategory] = resource
        store.dispatchState(newState = store.state.copy(movieSectionMap = newMap))
    }

    fun clearAllData() {
        store.dispatchState(
            newState = store.state.copy(
                movieSectionMap = mutableMapOf(
                    MovieCategory.POPULAR to null,
                    MovieCategory.TOP_RATED to null,
                    MovieCategory.UP_COMING to null,
                    MovieCategory.NOW_PLAYING to null
                ),
                popularMovieFirstPage = null,
                nowPlayingMovieFirstPage = null,
                topRatedMovieFirstPage = null,
                upComingMovieFirstPage = null
            )
        )
    }

    fun setFavoriteList(favoriteList: List<FavoriteMovie>) {
        _favoriteList = favoriteList
    }

    fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

    fun setIsError(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isError = value))
    }

    fun setCurrentPageType(movieCategory: MovieCategory?) {
        store.dispatchState(newState = store.state.copy(currentPageType = movieCategory))
    }

    override fun onCleared() {
        loadMovieListJob?.cancel()
        loadFavoriteMovieJob?.cancel()
        insertFavoriteMovieJob?.cancel()
        super.onCleared()
    }
}