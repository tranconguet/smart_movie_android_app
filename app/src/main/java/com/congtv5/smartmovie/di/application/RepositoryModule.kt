package com.congtv5.smartmovie.di.application

import com.congtv5.data.data.local.FavoriteMovieDatabase
import com.congtv5.data.data.remote.DiscoverApi
import com.congtv5.data.data.remote.GenreApi
import com.congtv5.data.data.remote.MovieApi
import com.congtv5.data.data.remote.SearchApi
import com.congtv5.data.mapper.*
import com.congtv5.data.repository.FavoriteMovieRepositoryImpl
import com.congtv5.data.repository.GenreRepositoryImpl
import com.congtv5.data.repository.MovieRepositoryImpl
import com.congtv5.data.repository.SearchRepositoryImpl
import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.domain.repository.FavoriteMovieRepository
import com.congtv5.domain.repository.GenreRepository
import com.congtv5.domain.repository.MovieRepository
import com.congtv5.domain.repository.SearchRepository
import com.congtv5.smartmovie.SmartMovieApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieApi: MovieApi,
        dispatcherProvider: DispatcherProvider,
        movieDetailMapper: MovieDetailMapper,
        movieListPageMapper: MovieListPageMapper,
        castAndCrewMapper: CastAndCrewMapper
    ): MovieRepository = MovieRepositoryImpl(
        movieApi,
        dispatcherProvider,
        movieDetailMapper,
        movieListPageMapper,
        castAndCrewMapper
    )

    @Singleton
    @Provides
    fun provideGenreRepository(
        genreApi: GenreApi,
        discoverApi: DiscoverApi,
        dispatcherProvider: DispatcherProvider,
        genreMapper: GenreMapper,
        movieListPageMapper: MovieListPageMapper
    ): GenreRepository = GenreRepositoryImpl(genreApi, discoverApi, dispatcherProvider, genreMapper, movieListPageMapper)

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchApi: SearchApi,
        dispatcherProvider: DispatcherProvider,
        movieListPageMapper: MovieListPageMapper
    ): SearchRepository = SearchRepositoryImpl(searchApi, dispatcherProvider, movieListPageMapper)

    @Singleton
    @Provides
    fun provideFavoriteMovieRepository(
        context: SmartMovieApplication,
        dispatcherProvider: DispatcherProvider,
        favoriteMovieMapper: FavoriteMovieMapper
    ): FavoriteMovieRepository = FavoriteMovieRepositoryImpl(
        FavoriteMovieDatabase.getInstance(context).favoriteMovieDao(),
        dispatcherProvider,
        favoriteMovieMapper
    )
}