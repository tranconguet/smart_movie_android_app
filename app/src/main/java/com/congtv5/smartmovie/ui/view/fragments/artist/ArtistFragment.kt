package com.congtv5.smartmovie.ui.view.fragments.artist

import android.view.View
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment

class ArtistFragment : BaseFragment() {

    override fun getLayoutID(): Int {
        return R.layout.fragment_artist
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
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