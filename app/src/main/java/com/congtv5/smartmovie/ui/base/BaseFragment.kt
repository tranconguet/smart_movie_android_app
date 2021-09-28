package com.congtv5.smartmovie.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    abstract fun getLayoutID(): Int

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutID(), container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initObserveData()
        initData()
        initView()
        initAction()
    }

    abstract fun initBinding(view: View)

    abstract fun initObserveData()

    abstract fun initData()

    abstract fun initView()

    abstract fun initAction()

}