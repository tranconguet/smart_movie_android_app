package com.congtv5.smartmovie.di.application

import com.congtv5.smartmovie.SmartMovieApplication
import com.congtv5.smartmovie.di.screen.FragmentComponent
import com.congtv5.smartmovie.di.screen.FragmentModule
import com.congtv5.smartmovie.di.screen.ActivityComponent
import com.congtv5.smartmovie.di.screen.ActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ApiModule::class, RepositoryModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(application: SmartMovieApplication)

    fun plus(screenModule: ActivityModule): ActivityComponent

    fun plus(fragmentModule: FragmentModule): FragmentComponent
}