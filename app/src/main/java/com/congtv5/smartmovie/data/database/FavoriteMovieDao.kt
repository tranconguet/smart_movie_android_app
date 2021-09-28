package com.congtv5.smartmovie.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import com.congtv5.smartmovie.utils.Constants.FAVORITE_MOVIE_TABLE
import com.congtv5.smartmovie.utils.Constants.MOVIE_ID
import com.congtv5.smartmovie.utils.Constants.MOVIE_IS_LIKED
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM $FAVORITE_MOVIE_TABLE")
    fun getFavoriteMovie(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM $FAVORITE_MOVIE_TABLE WHERE $MOVIE_ID=:movieId")
    fun getFavoriteMovie(movieId: Int): FavoriteMovieEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(vararg movies: FavoriteMovieEntity)

    @Query("UPDATE $FAVORITE_MOVIE_TABLE SET $MOVIE_IS_LIKED=:value WHERE $MOVIE_ID=:movieId")
    fun updateFavoriteMovie(movieId: Int, value: Boolean)

}