package com.congtv5.smartmovie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.congtv5.smartmovie.data.database.entity.FavoriteMovieEntity
import com.congtv5.smartmovie.utils.Constants.FAVORITE_MOVIE_DATABASE

@Database(
    entities = [
        FavoriteMovieEntity::class
    ],
    version = 1
)
abstract class FavoriteMovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteMovieDatabase? = null

        fun getInstance(context: Context): FavoriteMovieDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteMovieDatabase::class.java,
                    FAVORITE_MOVIE_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}