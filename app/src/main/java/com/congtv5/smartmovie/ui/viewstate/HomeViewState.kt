package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType

data class HomeViewState(
    val isLoading: Boolean,
    val isError: Boolean,
    val currentPageType: MovieCategory?,
    val currentDisplayType: MovieItemDisplayType,
    val favoriteList: List<FavoriteMovie>,
    val movieSectionMap: Map<MovieCategory, Resource<List<Movie>>?>,
    val popularMovieFirstPage: MovieListPage?,
    val topRatedMovieFirstPage: MovieListPage?,
    val nowPlayingMovieFirstPage: MovieListPage?,
    val upComingMovieFirstPage: MovieListPage?
)