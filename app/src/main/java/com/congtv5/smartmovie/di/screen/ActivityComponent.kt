package com.congtv5.smartmovie.di.screen

import com.congtv5.smartmovie.di.scope.PerScreen
import com.congtv5.smartmovie.ui.view.MainActivity
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: MainActivity)

}