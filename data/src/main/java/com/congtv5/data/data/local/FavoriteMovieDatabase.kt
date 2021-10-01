package com.congtv5.data.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.congtv5.data.data.local.entity.FavoriteMovieEntity
import com.congtv5.data.utils.Constants.FAVORITE_MOVIE_DATABASE

@Database(
    entities = [
        FavoriteMovieEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FavoriteMovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteMovieDatabase? = null

        fun getInstance(context: Context): FavoriteMovieDatabase {
            return INSTANCE ?: synchronized(this) {
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