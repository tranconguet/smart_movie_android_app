package com.congtv5.domain.model

data class Genre(
    val id: Int,
    val name: String,
    var backdropPath: String?, // background image url
)