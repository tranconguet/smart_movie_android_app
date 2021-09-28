package com.congtv5.smartmovie.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.congtv5.smartmovie.utils.Constants.FAVORITE_MOVIE_TABLE
import com.congtv5.smartmovie.utils.Constants.MOVIE_ID
import com.congtv5.smartmovie.utils.Constants.MOVIE_IS_LIKED

@Entity(
    tableName = FAVORITE_MOVIE_TABLE
)
class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = MOVIE_IS_LIKED)
    val isLiked: Boolean
)