package com.congtv5.smartmovie.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetGenreListUseCase
import com.congtv5.domain.usecase.GetSearchMovieListPageUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseViewModel
import com.congtv5.smartmovie.ui.viewstate.SearchViewState
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getSearchMovieListPageUseCase: GetSearchMovieListPageUseCase
) : BaseViewModel<SearchViewState>() {

    override fun initState(): SearchViewState {
        return SearchViewState(
            currentQuery = EMPTY_TEXT,
            genreList = listOf(),
            currentPage = 0,
            isLoading = false,
            isLoadingMore = false,
            moviePages = listOf()
        )
    }

    fun getGenreList() {
        viewModelScope.launch {
            when (val genresResource = getGenreListUseCase.execute()) {
                is Resource.Success -> {
                    genresResource.data?.let { newList ->
                        Log.d("CongTV5", "SearchViewModel #getGenreList success")
                        store.dispatchState(newState = store.state.copy(genreList = newList))
                    }
                }
                else -> {
                    Log.e("CongTV5", "SearchViewModel #getGenreList error")
                }
            }
        }
    }

    fun getNextMovieListPage() {
        if (store.state.moviePages.isNotEmpty()) {
            setIsLoadingMore(true) // load more
        } else {
            setIsLoading(true) // load first time
        }
        viewModelScope.launch {
            when (val moviesResource =
                getSearchMovieListPageUseCase.execute(
                    store.state.currentQuery,
                    store.state.currentPage + 1,
                )) {
                is Resource.Success -> {
                    moviesResource.data?.let { newList ->
                        Log.d("CongTV5", "SearchViewModel #getNextMovieListPage newList $newList")
                        increasePage()
                        addResultListPage(newList)
                    }
                    if (store.state.isLoadingMore) {
                        setIsLoadingMore(false)
                    } else {
                        setIsLoading(false)
                    }
                }
                is Resource.Error -> {
                    Log.e("CongTV5", "SearchViewModel #getNextMovieListPage error")
                    if (store.state.isLoadingMore) {
                        setIsLoadingMore(false)
                    } else {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

    private fun addResultListPage(newList: MovieListPage) {
        val list = mutableListOf<MovieListPage>()
        list.addAll(store.state.moviePages)
        list.add(newList)
        store.dispatchState(newState = store.state.copy(moviePages = list))
    }

    private fun increasePage() {
        store.dispatchState(newState = store.state.copy(currentPage = store.state.currentPage + 1))
    }

    fun setCurrentQuery(value: String) {
        store.dispatchState(newState = store.state.copy(currentQuery = value))
    }

    fun getGenreNameById(genreId: Int): String? {
        return store.state.genreList.find { genre -> genre.id == genreId }?.name
    }

    fun clearResult() {
        store.dispatchState(newState = store.state.copy(currentPage = 0, moviePages = listOf()))
    }

    fun setIsLoadingMore(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoadingMore = value))
    }

    fun setIsLoading(value: Boolean) {
        store.dispatchState(newState = store.state.copy(isLoading = value))
    }

}


//class SearchViewModel @Inject constructor(
//    private val getGenreListUseCase: GetGenreListUseCase,
//    private val getSearchMovieListPageUseCase: GetSearchMovieListPageUseCase
//) : ViewModel() {
//
//    private var _currentQuery = MutableStateFlow(EMPTY_TEXT)
//    val currentQuery: StateFlow<String> = _currentQuery
//
//    private var _genreList = MutableStateFlow<List<Genre>>(listOf())
//    val genreList: StateFlow<List<Genre>> = _genreList
//
//    private var _currentPage = MutableStateFlow(0)
//    val currentPage: StateFlow<Int> = _currentPage
//
//    private var _isLoadingMore = MutableStateFlow(false)
//    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore
//
//    private var _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private var _resultListPages = MutableStateFlow(mutableListOf<MovieListPage>())
//    val resultListPages: StateFlow<List<MovieListPage>> = _resultListPages
//
//    fun getGenreList() {
//        viewModelScope.launch {
//            when (val genresResource = getGenreListUseCase.execute()) {
//                is Resource.Success -> {
//                    genresResource.data?.let { newList ->
//                        _genreList.value = newList
//                    }
//                }
//                else -> {
//                    Log.e("CongTV5", "SearchViewModel #getGenreList error when get genreList")
//                }
//            }
//        }
//    }
//
//    fun getNextMovieListPage() {
//        if (_resultListPages.value.isNotEmpty()) {
//            setIsLoadingMore(true) // load more
//        } else {
//            setIsLoading(true) // load first time
//        }
//        viewModelScope.launch {
//            when (val moviesResource =
//                getSearchMovieListPageUseCase.execute(
//                    currentQuery.value,
//                    _currentPage.value + 1,
//                )) {
//                is Resource.Success -> {
//                    moviesResource.data?.let { newList ->
//                        increasePage()
//                        addResultListPage(newList)
//                    }
//                    if (_isLoadingMore.value) {
//                        setIsLoadingMore(false)
//                    } else {
//                        setIsLoading(false)
//                    }
//                }
//                else -> {
//                    if (_isLoadingMore.value) {
//                        setIsLoadingMore(false)
//                    } else {
//                        setIsLoading(false)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun addResultListPage(newList: MovieListPage) {
//        val list = mutableListOf<MovieListPage>()
//        list.addAll(_resultListPages.value)
//        list.add(newList)
//        _resultListPages.value = list
//    }
//
//    private fun increasePage() {
//        _currentPage.value = _currentPage.value + 1
//    }
//
//    fun setCurrentQuery(value: String) {
//        _currentQuery.value = value
//    }
//
//    fun getGenreNameById(genreId: Int): String? {
//        return _genreList.value.find { genre -> genre.id == genreId }?.name
//    }
//
//    fun clearResult() {
//        _currentPage.value = 0
//        _resultListPages.value.clear()
//    }
//
//    fun setIsLoadingMore(value: Boolean) {
//        _isLoadingMore.value = value
//    }
//
//    fun setIsLoading(value: Boolean) {
//        _isLoading.value = value
//    }
//
//}