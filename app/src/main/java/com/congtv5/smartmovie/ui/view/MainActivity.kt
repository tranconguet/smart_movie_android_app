package com.congtv5.smartmovie.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.congtv5.smartmovie.ui.viewmodel.MainActivityViewModel
import com.congtv5.smartmovie.utils.MovieItemDisplayType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        toolbar = binding.tbMain
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = binding.bnMain

        val navController = findNavController(R.id.fragmentContainerView)

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.fHome, R.id.fGenre))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }

}