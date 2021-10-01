package com.congtv5.smartmovie.ui.view.fragments.search

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.congtv5.domain.model.Movie
import com.congtv5.domain.model.MovieListPage
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.base.fragment.BaseFragment
import com.congtv5.smartmovie.ui.view.adapter.SearchResultListAdapter
import com.congtv5.smartmovie.ui.view.fragments.home.HomeFragment
import com.congtv5.smartmovie.ui.viewmodel.SearchViewModel
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    private lateinit var edtSearch: EditText
    private lateinit var tvCancel: TextView
    private lateinit var rvSearchResultList: RecyclerView
    private lateinit var prbLoadMore: ProgressBar
    private lateinit var prbSearch: ProgressBar

    private var isScrolling = false // for load more
    private var totalItemNumber = 0
    private var scrollOutItemNumber = 0

    @Inject
    lateinit var searchViewModel: SearchViewModel
    private var searchListAdapter: SearchResultListAdapter? = null

    override fun getLayoutID(): Int {
        return R.layout.fragment_search
    }

    override fun initInjection() {
        fragmentComponent.inject(this)
    }

    override fun initBinding(view: View) {
        edtSearch = view.findViewById(R.id.edtSearch)
        tvCancel = view.findViewById(R.id.tvCancel)
        rvSearchResultList = view.findViewById(R.id.rvSearchResultList)
        prbLoadMore = view.findViewById(R.id.prbLoadMore)
        prbSearch = view.findViewById(R.id.prbSearch)
    }

    override fun initObserveData() {

        searchViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.moviePages },
            observer = { movieListPage ->
                Log.d("CongTV5", "SearchFragment #initObserveData moviePage $movieListPage")
                updateMovieList(movieListPage)
            }
        )

        searchViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                Log.d("CongTV5", "SearchFragment #initObserveData isLoading $isLoading")
                setProgressBar(isLoading)
            }
        )

        searchViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.isLoadingMore },
            observer = { isLoadingMore ->
                Log.d("CongTV5", "SearchFragment #initObserveData isLoadingMore $isLoadingMore")
                loadingMore(isLoadingMore)
            }
        )

    }

    override fun initData() {
        searchViewModel.getGenreList()
    }

    override fun initView() {
        initAdapter()
    }

    private fun initAdapter() {
        rvSearchResultList.layoutManager = LinearLayoutManager(context)
        searchListAdapter =
            SearchResultListAdapter({ movieId: Int -> goToMovieDetailPage(movieId) }, { id ->
                getGenreNameById(id)
            })
        rvSearchResultList.adapter = searchListAdapter
    }

    override fun initAction() {
        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMovie()
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
        tvCancel.setOnClickListener {
            hideKeyboard()
            clearEditText()
        }
        initScrollAction()
    }

    private fun searchMovie() {
        hideKeyboard()
        if (edtSearch.text.isNotEmpty()) {
            searchViewModel.clearResult()
            searchViewModel.setCurrentQuery(edtSearch.text.toString())
            searchViewModel.getNextMovieListPage()
        }
    }

    private fun getGenreNameById(genreId: Int): String? {
        return searchViewModel.getGenreNameById(genreId)
    }

    private fun clearEditText() {
        edtSearch.text.clear()
    }

    private fun updateMovieList(movieListPagers: List<MovieListPage>) {
        val movieList = mutableListOf<Movie>()
        movieListPagers.forEach { movieList.addAll(it.results) }
        searchListAdapter?.submitList(movieList)
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
        (imm as? InputMethodManager)?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun initScrollAction() {
        rvSearchResultList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentLayoutManager = rvSearchResultList.layoutManager
                scrollOutItemNumber = when (currentLayoutManager) {
                    is LinearLayoutManager -> (currentLayoutManager as? LinearLayoutManager?)?.findLastVisibleItemPosition()
                        ?: 0
                    else -> (currentLayoutManager as? GridLayoutManager?)?.findLastVisibleItemPosition()
                        ?: 0
                }
                if (scrollOutItemNumber != -1) {
                    if (isLoadingMorePosition() && !searchViewModel.store.state.isLoadingMore) {
                        searchViewModel.getNextMovieListPage()
                    }
                }
            }
        })
    }

    private fun isLoadingMorePosition(): Boolean {
        // calculate correct position
        val listOfMovieList = searchViewModel.store.state.moviePages
            .map { it.results.size }
        Log.d("CongTV5", "SearchFragment #isLoadingMorePosition $listOfMovieList")
        if (listOfMovieList.isEmpty()) return false
        totalItemNumber = listOfMovieList.reduce { accumulator, value -> accumulator + value }
        return isScrolling && scrollOutItemNumber + HomeFragment.GRID_ITEM_PER_ROW >= totalItemNumber
    }

    private fun setProgressBar(isLoading: Boolean) {
        if (isLoading) {
            prbSearch.visibility = View.VISIBLE
        } else {
            prbSearch.visibility = View.INVISIBLE
        }
    }

    private fun loadingMore(isLoadingMore: Boolean) {
        if (isLoadingMore) {
            prbLoadMore.visibility = View.VISIBLE
        } else {
            prbLoadMore.visibility = View.GONE
        }
    }


}