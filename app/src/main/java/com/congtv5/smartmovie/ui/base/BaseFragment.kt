package com.congtv5.smartmovie.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<out T : ViewBinding> : Fragment() {

    private var _binding: T? = null

    val binding
        get() = _binding ?: throw IllegalStateException(
            "binding is only valid between onCreateView and onDestroyView"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T?

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserveData()
        initData()
        initView()
        initAction()
    }

    abstract fun initObserveData()

    abstract fun initData()

    abstract fun initView()

    abstract fun initAction()

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}