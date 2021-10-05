package com.congtv5.smartmovie.di.application

import com.congtv5.data.data.remote.DiscoverApi
import com.congtv5.data.data.remote.GenreApi
import com.congtv5.data.data.remote.MovieApi
import com.congtv5.data.data.remote.SearchApi
import com.congtv5.smartmovie.utils.Constants
import com.congtv5.smartmovie.utils.Constants.TIME_OUT
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideMovieApi(client: OkHttpClient): MovieApi {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.MOVIE_BASE_URL)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGenreApi(client: OkHttpClient): GenreApi {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.GENRE_BASE_URL)
            .build()
            .create(GenreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApi(client: OkHttpClient): SearchApi {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.SEARCH_BASE_URL)
            .build()
            .create(SearchApi::class.java)
    }


    @Provides
    @Singleton
    fun provideDiscoverApi(client: OkHttpClient): DiscoverApi {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.DISCOVER_BASE_URL)
            .build()
            .create(DiscoverApi::class.java)
    }

}