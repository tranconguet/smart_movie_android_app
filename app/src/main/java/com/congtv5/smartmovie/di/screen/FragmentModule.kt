package com.congtv5.smartmovie.di.screen

import com.congtv5.smartmovie.di.scope.PerFragment
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: BaseFragment) {

    @PerFragment
    @Provides
    fun provideFragment(): BaseFragment {
        return fragment
    }

}