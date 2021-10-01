package com.congtv5.smartmovie.di.screen

import com.congtv5.smartmovie.di.scope.PerFragment
import com.congtv5.smartmovie.ui.view.fragments.artist.ArtistFragment
import com.congtv5.smartmovie.ui.view.fragments.genre.GenreFragment
import com.congtv5.smartmovie.ui.view.fragments.genre.MovieByGenreFragment
import com.congtv5.smartmovie.ui.view.fragments.home.*
import com.congtv5.smartmovie.ui.view.fragments.moviedetail.MovieDetailFragment
import com.congtv5.smartmovie.ui.view.fragments.search.SearchFragment
import dagger.Subcomponent

@PerFragment
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: AllMovieFragment)
    fun inject(fragment: NowPlayingMovieFragment)
    fun inject(fragment: PopularMovieFragment)
    fun inject(fragment: TopRatedMovieFragment)
    fun inject(fragment: UpComingMovieFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: ArtistFragment)
    fun inject(fragment: GenreFragment)
    fun inject(fragment: MovieDetailFragment)
    fun inject(fragment: MovieByGenreFragment)
}