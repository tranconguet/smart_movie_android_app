package com.congtv5.smartmovie.utils

import androidx.fragment.app.Fragment
import com.congtv5.domain.model.Genre
import com.congtv5.smartmovie.ui.view.fragments.home.NowPlayingMovieFragment
import com.congtv5.smartmovie.ui.view.fragments.home.PopularMovieFragment
import com.congtv5.smartmovie.ui.view.fragments.home.TopRatedMovieFragment
import com.congtv5.smartmovie.ui.view.fragments.home.UpComingMovieFragment
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import java.text.DateFormatSymbols
import java.util.*

fun getFragmentByMovieCategory(movieCategory: MovieCategory): Fragment {
    return when (movieCategory) {
        MovieCategory.POPULAR -> PopularMovieFragment()
        MovieCategory.TOP_RATED -> TopRatedMovieFragment()
        MovieCategory.UP_COMING -> UpComingMovieFragment()
        MovieCategory.NOW_PLAYING -> NowPlayingMovieFragment()
    }
}

fun formatRatingToString(rating: Double): String {
    return "$rating / 10"
}

fun formatGenresToString(genres: List<Genre>): String {
    return if (genres.isNotEmpty()) {
        genres.joinToString("") { genre ->
            "${genre.name} | "
        }.dropLast(2)
    } else {
        EMPTY_TEXT
    }
}

fun formatLanguageToString(language: String): String {
    return "Language: $language"
}

fun formatMovieDurationToString(
    runtime: Int,
    releaseDay: String,
    productionCountry: String
): String {
    val movieDuration = formatTime(runtime)
    val releaseDate = formatDate(releaseDay)
    val country = formatCountry(productionCountry)
    return listOf(releaseDate, country, movieDuration).joinToString(" ")
}

fun formatCountry(country: String): String{
    return if(country.isNotBlank()){
        "(${country})"
    }else{
        EMPTY_TEXT
    }
}

fun formatDate(str: String): String {
    val list = str.split("-")
    // [year,date,month]
    return if (list.size == 3) {
        val year = list[0]
        val month = DateFormatSymbols(Locale.ENGLISH).months[(list[1].toIntOrNull() ?: 1) - 1]
        val date = list[2]
        "$month ${date}, $year"
    } else {
        // api result fail
        EMPTY_TEXT
    }
}

fun formatTime(number: Int): String {
    val hours = number / 60
    val minutes = number - hours * 60
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        hours == 0 -> "${minutes}m"
        else -> EMPTY_TEXT // api result fail
    }
}