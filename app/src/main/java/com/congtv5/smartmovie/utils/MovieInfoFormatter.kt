package com.congtv5.smartmovie.utils

import com.congtv5.domain.model.Genre
import java.text.DateFormatSymbols
import java.util.*

class MovieInfoFormatter {

    fun formatRatingToString(rating: Double): String {
        return "$rating / 10"
    }

    fun formatGenresToString(genres: List<Genre>): String {
        return if (genres.isNotEmpty()) {
            genres.joinToString("") { genre ->
                "${genre.name} | "
            }.dropLast(2)
        } else {
            Constants.EMPTY_TEXT
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

    private fun formatCountry(country: String): String {
        return if (country.isNotBlank()) {
            "(${country})"
        } else {
            Constants.EMPTY_TEXT
        }
    }

    private fun formatDate(str: String): String {
        val list = str.split("-")
        // [year,date,month]
        return if (list.size == 3) {
            val year = list[0]
            val month = DateFormatSymbols(Locale.ENGLISH).months[(list[1].toIntOrNull() ?: 1) - 1]
            val date = list[2]
            "$month ${date}, $year"
        } else {
            // api result fail
            Constants.EMPTY_TEXT
        }
    }

    private fun formatTime(number: Int): String {
        val hours = number / 60
        val minutes = number - hours * 60
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            hours == 0 -> "${minutes}m"
            else -> Constants.EMPTY_TEXT // api result fail
        }
    }

}