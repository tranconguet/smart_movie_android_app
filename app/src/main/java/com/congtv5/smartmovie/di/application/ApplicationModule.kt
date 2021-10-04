package com.congtv5.smartmovie.di.application

import com.congtv5.data.utils.DispatcherProvider
import com.congtv5.data.utils.StandardDispatchers
import com.congtv5.smartmovie.SmartMovieApplication
import com.congtv5.smartmovie.utils.MovieInfoFormatter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: SmartMovieApplication) {

    @Provides
    @Singleton
    fun provideApplication(): SmartMovieApplication{
        return application
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatchers()

    @Singleton
    @Provides
    fun provideMovieInfo(): MovieInfoFormatter = MovieInfoFormatter()

}