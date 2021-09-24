package com.congtv5.smartmovie.data.model.ui

import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.utils.MovieCategory

data class MovieSection(val sectionType: MovieCategory, val movieList: List<Result>)