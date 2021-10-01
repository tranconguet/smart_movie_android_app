package com.congtv5.smartmovie.di.application

import com.congtv5.data.data.remote.DiscoverApi
import com.congtv5.data.data.remote.GenreApi
import com.congtv5.data.data.remote.MovieApi
import com.congtv5.data.data.remote.SearchApi
import com.congtv5.smartmovie.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.MOVIE_BASE_URL)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGenreApi(): GenreApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.GENRE_BASE_URL)
            .build()
            .create(GenreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApi(): SearchApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.SEARCH_BASE_URL)
            .build()
            .create(SearchApi::class.java)
    }


    @Provides
    @Singleton
    fun provideDiscoverApi(): DiscoverApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.DISCOVER_BASE_URL)
            .build()
            .create(DiscoverApi::class.java)
    }

}