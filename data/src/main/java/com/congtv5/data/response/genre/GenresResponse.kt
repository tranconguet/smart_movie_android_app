package com.congtv5.data.response.genre

import com.congtv5.data.response.movie.GenreResponse
import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @field:SerializedName("genres") val genres: List<GenreResponse>?
)