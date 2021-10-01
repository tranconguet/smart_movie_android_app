package com.congtv5.smartmovie.di.application

import com.congtv5.smartmovie.SmartMovieApplication
import com.congtv5.smartmovie.di.screen.FragmentComponent
import com.congtv5.smartmovie.di.screen.FragmentModule
import com.congtv5.smartmovie.di.screen.ScreenComponent
import com.congtv5.smartmovie.di.screen.ScreenModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ApiModule::class, RepositoryModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(application: SmartMovieApplication)

    fun plus(screenModule: ScreenModule): ScreenComponent

    fun plus(fragmentModule: FragmentModule): FragmentComponent
}