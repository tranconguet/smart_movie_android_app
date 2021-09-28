package com.congtv5.smartmovie.di

import android.content.Context
import com.congtv5.smartmovie.data.database.FavoriteMovieDatabase
import com.congtv5.smartmovie.data.remote.api.GenreApi
import com.congtv5.smartmovie.data.remote.api.MovieApi
import com.congtv5.smartmovie.data.remote.api.SearchApi
import com.congtv5.smartmovie.data.repository.FavoriteMovieRepository
import com.congtv5.smartmovie.data.repository.GenreRepository
import com.congtv5.smartmovie.data.repository.MovieRepository
import com.congtv5.smartmovie.data.repository.SearchRepository
import com.congtv5.smartmovie.utils.Constants.GENRE_BASE_URL
import com.congtv5.smartmovie.utils.Constants.MOVIE_BASE_URL
import com.congtv5.smartmovie.utils.Constants.SEARCH_BASE_URL
import com.congtv5.smartmovie.utils.DispatcherProvider
import com.congtv5.smartmovie.utils.StandardDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideSearchApi(): SearchApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SEARCH_BASE_URL)
            .build()
            .create(SearchApi::class.java)
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

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchApi: SearchApi,
        dispatcherProvider: DispatcherProvider
    ): SearchRepository = SearchRepository(searchApi, dispatcherProvider)

    @Singleton
    @Provides
    fun provideFavoriteMovieRepository(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider
    ): FavoriteMovieRepository = FavoriteMovieRepository(
        FavoriteMovieDatabase.getInstance(context).favoriteMovieDao(),
        dispatcherProvider
    )

}