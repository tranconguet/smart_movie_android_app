package com.congtv5.smartmovie.di

import com.congtv5.smartmovie.data.remote.api.GenreApi
import com.congtv5.smartmovie.data.remote.api.MovieApi
import com.congtv5.smartmovie.data.repository.GenreRepository
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.utils.Constants.GENRE_BASE_URL
import com.congtv5.smartmovie.utils.Constants.MOVIE_BASE_URL
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.StandardDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatchers()

    @Singleton
    @Provides
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MOVIE_BASE_URL)
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGenreApi(): GenreApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GENRE_BASE_URL)
            .build()
            .create(GenreApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieApi: MovieApi,
        dispatcherProvider: DispatcherProvider
    ): MovieRepository = MovieRepository(movieApi, dispatcherProvider)

    @Singleton
    @Provides
    fun provideGenreRepository(
        genreApi: GenreApi,
        dispatcherProvider: DispatcherProvider
    ): GenreRepository = GenreRepository(genreApi, dispatcherProvider)

}