package com.congtv5.smartmovie.ui.viewmodel.home

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetMovieDetailUseCase
import com.congtv5.domain.usecase.GetUpComingMovieListPageUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseMovieListViewModel
import javax.inject.Inject

class UpComingListViewModel @Inject constructor(
    private val getUpComingMovieListPageUseCase: GetUpComingMovieListPageUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : BaseMovieListViewModel() {

    override suspend fun getMovieListPage(page: Int): Resource<MovieListPage> {
        return getUpComingMovieListPageUseCase.execute(page)
    }

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return getMovieDetailUseCase.execute(movieId)
    }

}