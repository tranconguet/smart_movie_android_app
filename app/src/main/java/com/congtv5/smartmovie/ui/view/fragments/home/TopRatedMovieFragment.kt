package com.congtv5.smartmovie.ui.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.congtv5.smartmovie.databinding.FragmentTopRatedMovieBinding
import com.congtv5.smartmovie.ui.base.BaseFragment

class TopRatedMovieFragment : BaseFragment<FragmentTopRatedMovieBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentTopRatedMovieBinding {
        return FragmentTopRatedMovieBinding.inflate(inflater, container, false)
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