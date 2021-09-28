package com.congtv5.smartmovie.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.data.repository.FavoriteMovieRepository
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var movieRepository: MovieRepository,
    private var favoriteMovieRepository: FavoriteMovieRepository
) : ViewModel() {

    companion object {
        const val ITEM_PER_SECTION = 4
    }

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private var _currentPage = MutableStateFlow<MovieCategory?>(null)
    val currentPage: StateFlow<MovieCategory?> = _currentPage

    private var _currentItemDisplayType = MutableStateFlow(MovieItemDisplayType.GRID)
    val currentDisplayType: StateFlow<MovieItemDisplayType> = _currentItemDisplayType

    private var _favoriteList = MutableStateFlow<List<FavoriteMovieEntity>>(listOf())
    val favoriteList: StateFlow<List<FavoriteMovieEntity>> = _favoriteList

    private var _allMovieSections = MutableStateFlow(
        mutableMapOf<MovieCategory, Resource<List<Result>>?>(
            MovieCategory.POPULAR to null,
            MovieCategory.TOP_RATED to null,
            MovieCategory.UP_COMING to null,
            MovieCategory.NOW_PLAYING to null
        )
    )
    val allMovieSections: StateFlow<Map<MovieCategory, Resource<List<Result>>?>> = _allMovieSections


    private var _popularMovieListPages = MutableStateFlow<MovieListPage?>(null)
    val popularMovieListPages: StateFlow<MovieListPage?> = _popularMovieListPages

    private var _topRatedMovieListPages = MutableStateFlow<MovieListPage?>(null)
    val topRatedMovieListPages: StateFlow<MovieListPage?> = _topRatedMovieListPages

    private var _upComingMovieListPages = MutableStateFlow<MovieListPage?>(null)
    val upComingMovieListPages: StateFlow<MovieListPage?> = _upComingMovieListPages

    private var _nowPlayingMovieListPages = MutableStateFlow<MovieListPage?>(null)
    val nowPlayingMovieListPages: StateFlow<MovieListPage?> = _nowPlayingMovieListPages

    init {
        clearAllData()
        setIsLoading(true)
        getMovieListToInitAllPage()
    }

    fun getFavoriteList() {
        viewModelScope.launch {
            favoriteMovieRepository.getFavoriteMovieList().collect { favList ->
                _favoriteList.value = favList
            }
        }
    }

    fun updateFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        viewModelScope.launch {
            if (isMovieFavorite(favoriteMovieEntity.movieId))
                favoriteMovieRepository.updateFavoriteMovie(favoriteMovieEntity)
            else favoriteMovieRepository.insertFavoriteMovie(favoriteMovieEntity)
        }
    }

    fun isMovieFavorite(movieId: Int): Boolean {
        val favList = _favoriteList.value.filter { item ->
            item.isLiked
        }.map { item ->
            item.movieId
        }
        return favList.contains(movieId)
    }

    fun getMovieListToInitAllPage() {
        viewModelScope.launch {
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
        when (val moviesResource = movieRepository.getPopularMovieList(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { newList ->
                    _popularMovieListPages.value = newList
                    val movieListForSection = if (newList.results.size >= ITEM_PER_SECTION) {
                        newList.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        newList.results
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
        when (val moviesResource = movieRepository.getTopRatedMovieList(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { newList ->
                    _topRatedMovieListPages.value = newList
                    val movieListForSection = if (newList.results.size >= ITEM_PER_SECTION) {
                        newList.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        newList.results
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
        when (val moviesResource = movieRepository.getUpComingMovieList(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { newList ->
                    _upComingMovieListPages.value = newList
                    val movieListForSection = if (newList.results.size >= ITEM_PER_SECTION) {
                        newList.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        newList.results
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
        when (val moviesResource = movieRepository.getNowPlayingMovieList(1)) {
            is Resource.Success -> {
                moviesResource.data?.let { newList ->
                    _nowPlayingMovieListPages.value = newList
                    val movieListForSection = if (newList.results.size >= ITEM_PER_SECTION) {
                        newList.results.subList(0, ITEM_PER_SECTION)
                    } else {
                        // results from network less than 4
                        newList.results
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

    fun setDisplayType(value: MovieItemDisplayType) {
        if (!_isLoading.value)
            _currentItemDisplayType.value = value
    }

    private fun updateSectionMap(
        sectionCategory: MovieCategory,
        resource: Resource<List<Result>>?
    ) {
        val newMap = mutableMapOf<MovieCategory, Resource<List<Result>>?>()
        newMap.putAll(_allMovieSections.value)
        newMap[sectionCategory] = resource
        _allMovieSections.value = newMap
    }

    fun clearAllData() {
        _allMovieSections.value.clear()
        _allMovieSections.value = mutableMapOf(
            MovieCategory.POPULAR to null,
            MovieCategory.TOP_RATED to null,
            MovieCategory.UP_COMING to null,
            MovieCategory.NOW_PLAYING to null
        )
        _popularMovieListPages.value = null
        _nowPlayingMovieListPages.value = null
        _topRatedMovieListPages.value = null
        _upComingMovieListPages.value = null
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun setIsError(value: Boolean) {
        _isError.value = value
    }

    fun setCurrentPage(movieCategory: MovieCategory?) {
        _currentPage.value = movieCategory
    }

}