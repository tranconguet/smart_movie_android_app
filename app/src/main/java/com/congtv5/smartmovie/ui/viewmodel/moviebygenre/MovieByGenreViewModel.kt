package com.congtv5.smartmovie.ui.viewmodel.moviebygenre

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetMovieDetailUseCase
import com.congtv5.domain.usecase.GetMovieListPageByGenreUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseMovieListViewModel
import javax.inject.Inject

class MovieByGenreViewModel @Inject constructor(
    private val getMovieListPageByGenreUseCase: GetMovieListPageByGenreUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : BaseMovieListViewModel() {

    private var genreId = 0

    override suspend fun getMovieListPage(page: Int): Resource<MovieListPage> {
        return getMovieListPageByGenreUseCase.execute(genreId, page)
    }

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return getMovieDetailUseCase.execute(movieId)
    }

    fun setGenreId(value: Int) {
        genreId = value
    }

}