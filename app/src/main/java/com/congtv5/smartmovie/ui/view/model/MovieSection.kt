package com.congtv5.smartmovie.ui.view.model

import com.congtv5.domain.model.Movie
import com.congtv5.smartmovie.utils.MovieCategory

data class MovieSection(val sectionType: MovieCategory, val movieList: List<Movie>)
