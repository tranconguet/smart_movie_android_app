package com.congtv5.smartmovie.ui.view.fragments

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.GenreListAdapter
import com.congtv5.smartmovie.ui.viewmodel.GenreListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GenreFragment : BaseFragment() {

    private val genreListViewModel: GenreListViewModel by viewModels()
    private var genreListAdapter: GenreListAdapter? = null

    private lateinit var rvGenreList: RecyclerView

    override fun getLayoutID(): Int {
        return R.layout.fragment_genre
    }

    override fun initBinding(view: View) {
        rvGenreList = view.findViewById(R.id.rvGenreList)
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
        rvGenreList.layoutManager = LinearLayoutManager(context)
        genreListAdapter = GenreListAdapter()
        rvGenreList.adapter = genreListAdapter
    }

    override fun initAction() {
    }

}