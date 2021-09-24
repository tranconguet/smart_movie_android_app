package com.congtv5.smartmovie.ui.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.congtv5.smartmovie.databinding.FragmentNowPlayingMovieBinding
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.MovieListAdapter
import com.congtv5.smartmovie.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect

class NowPlayingMovieFragment : BaseFragment<FragmentNowPlayingMovieBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNowPlayingMovieBinding {
        return FragmentNowPlayingMovieBinding.inflate(inflater, container, false)
    }

    override fun initObserveData() {
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initAction() {
    }
}