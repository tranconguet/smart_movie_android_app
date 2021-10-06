package com.congtv5.smartmovie.utils

import com.congtv5.domain.model.Genre
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import java.text.DateFormatSymbols
import java.util.*

fun Double.formatRatingToString(): String {
    return "$this / 10"
}

fun formatGenresToString(genres: List<Genre>): String {
    return if (genres.isNotEmpty()) {
        genres.joinToString("") { genre ->
            "${genre.name} | "
        }.dropLast(2) // remove last " |"
    } else {
        EMPTY_TEXT
    }
}

fun String.formatLanguageToString(): String {
    return "Language: $this"
}

fun formatMovieDurationToString(
    runtime: Int,
    releaseDay: String,
    productionCountry: String
): String {
    val movieDuration = runtime.formatTime()
    val releaseDate = releaseDay.formatDate()
    val country = productionCountry.formatCountry()
    return listOf(releaseDate, country, movieDuration).joinToString(" ")
}

fun String.formatCountry(): String {
    return if (this.isNotBlank()) {
        "($this)"
    } else {
        EMPTY_TEXT
    }
}

fun String.formatDate(): String {
    val list = this.split("-")
    // [year,date,month]
    return if (list.size == 3) {
        val year = list[0]
        val month = DateFormatSymbols(Locale.ENGLISH).months[(list[1].toIntOrNull() ?: 1) - 1]
        val date = list[2]
        "$month ${date}, $year"
    } else {
        EMPTY_TEXT // api result fail
    }
}

fun Int.formatTime(): String {
    val hours = this / 60
    val minutes = this - hours * 60
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        (hours == 0 && minutes > 0) -> "${minutes}m"
        else -> EMPTY_TEXT // api result fail
    }
}