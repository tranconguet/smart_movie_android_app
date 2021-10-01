package com.congtv5.smartmovie

import android.app.Application
import com.congtv5.smartmovie.di.application.ApplicationComponent
import com.congtv5.smartmovie.di.application.ApplicationModule
import com.congtv5.smartmovie.di.application.DaggerApplicationComponent

class SmartMovieApplication : Application() {

    lateinit var component: ApplicationComponent


    override fun onCreate() {
        super.onCreate()

        inject()
    }

    fun inject() {
        component = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(this)
        ).build()
        component.inject(this)
    }

}