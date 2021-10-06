package com.congtv5.smartmovie.di.screen

import com.congtv5.smartmovie.di.scope.PerScreen
import com.congtv5.smartmovie.ui.base.activity.BaseActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

    @PerScreen
    @Provides
    fun provideActivity(): BaseActivity {
        return activity
    }
}