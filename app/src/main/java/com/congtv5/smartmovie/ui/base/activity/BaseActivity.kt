package com.congtv5.smartmovie.ui.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.congtv5.smartmovie.SmartMovieApplication
import com.congtv5.smartmovie.di.application.ApplicationComponent
import com.congtv5.smartmovie.di.screen.ScreenModule

abstract class BaseActivity : AppCompatActivity() {

    val screenComponent by lazy {
        getApplicationComponent().plus(ScreenModule(this))
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (application as SmartMovieApplication).component
    }

    abstract fun getLayoutID(): Int
    abstract fun onCreateActivity(savedInstanceState: Bundle?)
    abstract fun initBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        initBinding()
        onCreateActivity(savedInstanceState)
    }

}