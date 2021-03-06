package com.congtv5.smartmovie.ui.viewmodel.home

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetMovieDetailUseCase
import com.congtv5.domain.usecase.GetNowPlayingMovieListPageUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseMovieListViewModel
import javax.inject.Inject

class NowPlayingListViewModel @Inject constructor(
    private val getNowPlayingMovieListPageUseCase: GetNowPlayingMovieListPageUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : BaseMovieListViewModel() {

    override suspend fun getMovieListPage(page: Int): Resource<MovieListPage> {
        return getNowPlayingMovieListPageUseCase.execute(page)
    }

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return getMovieDetailUseCase.execute(movieId)
    }

}