package com.congtv5.smartmovie.ui.viewmodel.home

import com.congtv5.domain.Resource
import com.congtv5.domain.model.MovieListPage
import com.congtv5.domain.usecase.GetTopRatedMovieListPageUseCase
import com.congtv5.smartmovie.ui.base.viewmodel.BaseMovieListViewModel
import javax.inject.Inject

class TopRatedListViewModel @Inject constructor(
    private val getTopRatedMovieListPageUseCase: GetTopRatedMovieListPageUseCase
) : BaseMovieListViewModel() {

    override suspend fun getMovieListPage(page: Int): Resource<MovieListPage> {
        return getTopRatedMovieListPageUseCase.execute(page)
    }

}