package com.congtv5.smartmovie.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.congtv5.smartmovie.data.model.pageresult.MovieListPage
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.ui.view.fragments.home.PopularMovieFragment
import com.congtv5.smartmovie.utils.Constants.NETWORK_ERROR_MESSAGE
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import com.congtv5.smartmovie.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var dispatcherProvider: DispatcherProvider,
    private var movieRepository: MovieRepository,
) : ViewModel() {

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private var _currentItemDisplayType = MutableStateFlow(MovieItemDisplayType.GRID)
    val currentDisplayType: StateFlow<MovieItemDisplayType> = _currentItemDisplayType

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
        clearSectionMap()
        getMovieListToInitAllPage()
    }

    fun getMovieListToInitAllPage() {
        setIsLoading(true)
        getPopularMovieListPageFirstLaunch()
        getTopRatedMovieListPageFirstLaunch()
        getUpComingMovieListPageFirstLaunch()
        getNowPlayingMovieListPageFirstLaunch()
    }

    fun getPopularMovieListPageFirstLaunch() {
        viewModelScope.launch {
            movieRepository.getPopularMovieList(1).collect { movieListPageResource ->
                Log.d("Testing", "1 done")
                when (movieListPageResource) {
                    is Resource.Success -> {
                        movieListPageResource.data?.let { newList ->
                            _popularMovieListPages.value = newList
                            val movieListForSection = newList.results.subList(0, 4)
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
        }
    }

    fun getTopRatedMovieListPageFirstLaunch() {
        viewModelScope.launch {
            movieRepository.getTopRatedMovieList(1).collect { movieListPageResource ->
                Log.d("Testing", "2 done")
                when (movieListPageResource) {
                    is Resource.Success -> {
                        movieListPageResource.data?.let { newList ->
                            _topRatedMovieListPages.value = newList
                            val movieListForSection = newList.results.subList(0, 4)
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
        }
    }

    fun getUpComingMovieListPageFirstLaunch() {
        viewModelScope.launch {
            movieRepository.getUpComingMovieList(1).collect { movieListPageResource ->
                Log.d("Testing", "3 done")
                when (movieListPageResource) {
                    is Resource.Success -> {
                        movieListPageResource.data?.let { newList ->
                            _upComingMovieListPages.value = newList
                            val movieListForSection = newList.results.subList(0, 4)
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
        }
    }

    fun getNowPlayingMovieListPageFirstLaunch() {
        viewModelScope.launch {
            movieRepository.getNowPlayingMovieList(1).collect { movieListPageResource ->
                Log.d("Testing", "4 done")
                when (movieListPageResource) {
                    is Resource.Success -> {
                        movieListPageResource.data?.let { newList ->
                            _nowPlayingMovieListPages.value = newList
                            val movieListForSection = newList.results.subList(0, 4)
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
        }
    }

    fun setDisplayType(value: MovieItemDisplayType) {
        if (!_isLoading.value)
            _currentItemDisplayType.value = value
    }

//    fun getTopRatedMovieListPage(page: Int) {
//        viewModelScope.launch {
//            movieRepository.getTopRatedMovieList(1).collect { movieListPageResource ->
//                when (movieListPageResource) {
//                    is Resource.Success -> {
//                        movieListPageResource.data?.let { newList ->
//                            _topRatedMovieListPages.value.add(newList)
//                        }
//                    }
//                    else -> {
//                        setIsError(true)
//                    }
//                }
//            }
//        }
//    }

//    fun getTopRatedMovieListPage(page: Int) {
//        viewModelScope.launch {
//            movieRepository.getTopRatedMovieList(1).collect { movieListPageResource ->
//                when (movieListPageResource) {
//                    is Resource.Success -> {
//                        movieListPageResource.data?.let { newList ->
//                            _topRatedMovieListPages.value.add(newList)
//                        }
//                    }
//                    else -> {
//                        setIsError(true)
//                    }
//                }
//            }
//        }
//    }
//
//    fun getNowPlayingMovieListPage(page: Int) {
//        viewModelScope.launch {
//            movieRepository.getNowPlayingMovieList(1).collect { movieListPageResource ->
//                when (movieListPageResource) {
//                    is Resource.Success -> {
//                        movieListPageResource.data?.let { newList ->
//                            _nowPlayingMovieListPages.value.add(newList)
//                        }
//                    }
//                    else -> {
//                        setIsError(true)
//                    }
//                }
//            }
//        }
//    }
//
//    fun getUpComingMovieListPage(page: Int) {
//        viewModelScope.launch {
//            movieRepository.getUpComingMovieList(1).collect { movieListPageResource ->
//                when (movieListPageResource) {
//                    is Resource.Success -> {
//                        movieListPageResource.data?.let { newList ->
//                            _upComingMovieListPages.value.add(newList)
//                        }
//                    }
//                    else -> {
//                        setIsError(true)
//                    }
//                }
//            }
//        }
//    }

    private fun updateSectionMap(
        sectionCategory: MovieCategory,
        resource: Resource<List<Result>>?
    ) {
        val newMap = mutableMapOf<MovieCategory, Resource<List<Result>>?>()
        newMap.putAll(_allMovieSections.value)
        newMap[sectionCategory] = resource
        _allMovieSections.value = newMap
    }

    fun clearSectionMap() {
        _allMovieSections?.value = mutableMapOf(
            MovieCategory.POPULAR to null,
            MovieCategory.TOP_RATED to null,
            MovieCategory.UP_COMING to null,
            MovieCategory.NOW_PLAYING to null
        )
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun setIsError(value: Boolean) {
        _isError.value = value
    }

}