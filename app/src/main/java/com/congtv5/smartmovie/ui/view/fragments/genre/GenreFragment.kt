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

    private lateinit var genreListRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var reloadTextView: TextView
    private lateinit var errorLayout: LinearLayout

    override fun getLayoutID(): Int {
        return R.layout.fragment_genre
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        genreListRecyclerView = view.findViewById(R.id.rvGenreList)
        loadingProgressBar = view.findViewById(R.id.prbLoading)
        reloadTextView = view.findViewById(R.id.tvReload)
        errorLayout = view.findViewById(R.id.layoutError)
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
            loadingProgressBar.visibility = View.VISIBLE
            errorLayout.visibility = View.INVISIBLE
        } else if (!isLoading && genreListViewModel.currentState.isError) {
            loadingProgressBar.visibility = View.INVISIBLE
            errorLayout.visibility = View.VISIBLE
        } else {
            loadingProgressBar.visibility = View.INVISIBLE
            errorLayout.visibility = View.INVISIBLE
        }
    }

    override fun initData() {
        genreListViewModel.getGenreList()
    }

    override fun initView() {
        genreListRecyclerView.layoutManager = LinearLayoutManager(context)
        genreListAdapter = GenreListAdapter { id, name ->
            genreOnClick(id, name)
        }
        genreListRecyclerView.adapter = genreListAdapter
    }

    override fun initAction() {
        reloadTextView.setOnClickListener {
            genreListViewModel.getGenreList()
        }
    }

    private fun genreOnClick(genreId: Int, genreTitle: String) {
        val action = GenreFragmentDirections.actionGenreFragmentToMovieByGenreFragment(genreId, genreTitle)
        findNavController().navigate(action)
    }

}