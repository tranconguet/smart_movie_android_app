package com.congtv5.smartmovie.utils

import com.congtv5.smartmovie.utils.Constants.NOW_PLAYING_TEXT
import com.congtv5.smartmovie.utils.Constants.POPULAR_TEXT
import com.congtv5.smartmovie.utils.Constants.TOP_RATED_TEXT
import com.congtv5.smartmovie.utils.Constants.UP_COMING_TEXT

enum class MovieCategory(val text: String) {
    POPULAR(POPULAR_TEXT),
    TOP_RATED(TOP_RATED_TEXT),
    UP_COMING(UP_COMING_TEXT),
    NOW_PLAYING(NOW_PLAYING_TEXT)
}