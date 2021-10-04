package com.congtv5.smartmovie.ui.viewmodel.genre

import androidx.lifecycle.MutableLiveData
import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetMovieListPageByGenreUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseMovieListViewModel
import javax.inject.Inject

class MovieByGenreViewModel @Inject constructor(
    private val getMovieListPageByGenreUseCase: GetMovieListPageByGenreUseCase
) : BaseMovieListViewModel() {

    private var _genreId = MutableLiveData(0)

    override suspend fun getMovieListPage(page: Int): Resource<MovieListPage> {
        return getMovieListPageByGenreUseCase.execute(_genreId.value ?: 0, page)
    }

    fun setGenreId(value: Int) {
        _genreId.value = value
    }

}