package com.congtv5.smartmovie.utils

object Constants {
    // hey
    const val API_KEY = "f83b37818a4c9a1957a3c8138333cbea"
    const val MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/"
    const val GENRE_BASE_URL = "https://api.themoviedb.org/3/genre/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
    const val SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/"
    const val MOVIES_TEXT = "Movies"
    const val POPULAR_TEXT = "Popular"
    const val TOP_RATED_TEXT = "Top Rated"
    const val NOW_PLAYING_TEXT = "Now Playing"
    const val UP_COMING_TEXT = "Up Coming"
    const val DISCOVER_TEXT = "Discover"
    const val SEARCH_TEXT = "Search"
    const val GENRE_TEXT = "Genres"
    const val ARTIST_TEXT = "Artist"
    const val MOVIE_LIST_TYPE = 5
    val movieListTypeNameList =
        listOf(MOVIES_TEXT, POPULAR_TEXT, TOP_RATED_TEXT, NOW_PLAYING_TEXT, UP_COMING_TEXT)
    const val NETWORK_ERROR_MESSAGE = "Network error"
    const val EMPTY_TEXT = ""
    const val FAVORITE_MOVIE_DATABASE = "favorite_movie_db"
    const val FAVORITE_MOVIE_TABLE = "favorite_movie"
    const val MOVIE_ID = "movie_id"
    const val MOVIE_IS_LIKED = "movie_is_liked"
}