package com.congtv5.smartmovie.ui.view.fragments

import com.congtv5.smartmovie.ui.base.BaseFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.congtv5.smartmovie.databinding.FragmentGenreBinding
import com.congtv5.smartmovie.ui.view.adapter.GenreListAdapter
import com.congtv5.smartmovie.ui.viewmodel.GenreListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GenreFragment : BaseFragment<FragmentGenreBinding>() {

    private val genreListViewModel: GenreListViewModel by viewModels()
    private var genreListAdapter: GenreListAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGenreBinding {
        return FragmentGenreBinding.inflate(inflater, container, false)
    }

    override fun initObserveData() {
        lifecycleScope.launchWhenResumed {
            genreListViewModel.genres.collect { genres ->
                genreListAdapter?.submitList(genres)
            }
        }
    }

    override fun initData() {
        genreListViewModel.getGenreList()
    }

    override fun initView() {
        binding.rvGenreList.layoutManager = LinearLayoutManager(context)
        genreListAdapter = context?.let { GenreListAdapter(it) }
        binding.rvGenreList.adapter = genreListAdapter
    }

    override fun initAction() {
    }

}