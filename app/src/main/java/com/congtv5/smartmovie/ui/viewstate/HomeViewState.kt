package com.congtv5.smartmovie.ui.viewstate

import com.congtv5.domain.Resource
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import kotlinx.coroutines.flow.Flow

data class HomeViewState(
    val isLoading: Boolean,
    val isError: Boolean,
    val currentPageType: MovieCategory?,
    val currentDisplayType: MovieItemDisplayType,
    val favoriteList: Flow<List<FavoriteMovie>>?,
    val movieSectionMap: MutableMap<MovieCategory, Resource<MovieListPage>?>,
)