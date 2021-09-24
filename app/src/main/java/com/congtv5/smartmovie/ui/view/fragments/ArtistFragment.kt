package com.congtv5.smartmovie.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.congtv5.smartmovie.databinding.FragmentArtistBinding
import com.congtv5.smartmovie.databinding.FragmentSearchBinding
import com.congtv5.smartmovie.ui.base.BaseFragment

class ArtistFragment : BaseFragment<FragmentArtistBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentArtistBinding {
        return FragmentArtistBinding.inflate(inflater, container, false)
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