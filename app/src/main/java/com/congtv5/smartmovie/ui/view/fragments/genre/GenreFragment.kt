package com.congtv5.smartmovie.ui.view.fragments.genre

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.GenreListAdapter
import com.congtv5.smartmovie.ui.viewmodel.genre.GenreListViewModel
import javax.inject.Inject

class GenreFragment : BaseFragment() {

    @Inject
    lateinit var genreListViewModel: GenreListViewModel

    private var genreListAdapter: GenreListAdapter? = null

    private lateinit var rvGenreList: RecyclerView
    private lateinit var prbLoading: ProgressBar
    private lateinit var tvReload: TextView
    private lateinit var layoutError: LinearLayout

    override fun getLayoutID(): Int {
        return R.layout.fragment_genre
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        rvGenreList = view.findViewById(R.id.rvGenreList)
        prbLoading = view.findViewById(R.id.prbLoading)
        tvReload = view.findViewById(R.id.tvReload)
        layoutError = view.findViewById(R.id.layoutError)
    }

    override fun initObserveData() {

        genreListViewModel.store.observe(
            owner = this,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                handleLoading(isLoading)
            }
        )

        genreListViewModel.store.observe(
            owner = this,
            selector = { state -> state.genres },
            observer = { genres ->
                genreListAdapter?.submitList(genres)
            }
        )

    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            prbLoading.visibility = View.VISIBLE
            layoutError.visibility = View.INVISIBLE
        } else if (!isLoading && genreListViewModel.currentState.isError) {
            prbLoading.visibility = View.INVISIBLE
            layoutError.visibility = View.VISIBLE
        } else {
            prbLoading.visibility = View.INVISIBLE
            layoutError.visibility = View.INVISIBLE
        }
    }

    override fun initData() {
        genreListViewModel.getGenreList()
    }

    override fun initView() {
        rvGenreList.layoutManager = LinearLayoutManager(context)
        genreListAdapter = GenreListAdapter { id, name ->
            genreOnClick(id, name)
        }
        rvGenreList.adapter = genreListAdapter
    }

    override fun initAction() {
        tvReload.setOnClickListener {
            genreListViewModel.getGenreList()
        }
    }

    private fun genreOnClick(genreId: Int, genreTitle: String) {
        val action =
            GenreFragmentDirections.actionGenreFragmentToMovieByGenreFragment(genreId, genreTitle)
        findNavController().navigate(action)
    }

}