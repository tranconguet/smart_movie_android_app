package com.congtv5.smartmovie.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.congtv5.smartmovie.SmartMovieApplication
import com.congtv5.smartmovie.di.application.ApplicationComponent
import com.congtv5.smartmovie.di.screen.FragmentModule


abstract class BaseFragment : Fragment() {

    val fragmentComponent by lazy {
        getApplicationComponent().plus(FragmentModule(this))
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (activity?.application as SmartMovieApplication).component
    }

    abstract fun getLayoutID(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjection()
        initData()
    }

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
        initView()
        initAction()
    }

    abstract fun initInjection()

    abstract fun initBinding(view: View)

    abstract fun initObserveData()

    abstract fun initData()

    abstract fun initView()

    abstract fun initAction()

}