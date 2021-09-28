package com.congtv5.smartmovie.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

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