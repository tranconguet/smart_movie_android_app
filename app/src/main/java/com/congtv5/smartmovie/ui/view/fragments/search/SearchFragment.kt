package com.congtv5.smartmovie.ui.view.fragments.search

import android.app.Activity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
import com.congtv5.smartmovie.ui.viewmodel.search.SearchViewModel
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    private lateinit var searchEditText: EditText
    private lateinit var cancelTextView: TextView
    private lateinit var resultListRecyclerView: RecyclerView
    private lateinit var loadMoreProgressBar: ProgressBar
    private lateinit var searchingProgressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var reloadTextView: TextView

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
        searchEditText = view.findViewById(R.id.edtSearch)
        cancelTextView = view.findViewById(R.id.tvCancel)
        resultListRecyclerView = view.findViewById(R.id.rvSearchResultList)
        loadMoreProgressBar = view.findViewById(R.id.prbLoadMore)
        searchingProgressBar = view.findViewById(R.id.prbLoading)
        errorLayout = view.findViewById(R.id.layoutError)
        reloadTextView = view.findViewById(R.id.tvReload)
    }

    override fun initObserveData() {

        searchViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.moviePages },
            observer = { movieListPage ->
                updateMovieList(movieListPage)
            }
        )

        searchViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.isLoading },
            observer = { isLoading ->
                setProgressBar(isLoading)
            }
        )

        searchViewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.isLoadingMore },
            observer = { isLoadingMore ->
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
        resultListRecyclerView.layoutManager = LinearLayoutManager(context)
        searchListAdapter = SearchResultListAdapter(
            { movieId: Int -> goToMovieDetailPage(movieId) },
            { id -> getGenreNameById(id) })
        resultListRecyclerView.adapter = searchListAdapter
    }

    override fun initAction() {
        initScrollAction()
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMovie()
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
        cancelTextView.setOnClickListener {
            hideKeyboard()
            clearEditText()
        }
        reloadTextView.setOnClickListener {
            searchMovie()
        }
    }

    private fun searchMovie() {
        hideKeyboard()
        if (searchEditText.text.isNotEmpty()) {
            searchViewModel.clearResult()
            searchViewModel.setCurrentQuery(searchEditText.text.toString())
            searchViewModel.getNextMovieListPage()
        }
    }

    private fun getGenreNameById(genreId: Int): String? {
        return searchViewModel.getGenreNameById(genreId)
    }

    private fun updateMovieList(movieListPagers: List<MovieListPage>) {
        val movieList = mutableListOf<Movie>()
        movieListPagers.forEach { movieList.addAll(it.results) }
        searchListAdapter?.submitList(movieList)
    }

    private fun initScrollAction() {
        resultListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentLayoutManager = resultListRecyclerView.layoutManager
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
        if (listOfMovieList.isEmpty()) return false
        totalItemNumber = listOfMovieList.reduce { accumulator, value -> accumulator + value }
        return isScrolling && scrollOutItemNumber + HomeFragment.GRID_ITEM_PER_ROW >= totalItemNumber
    }

    private fun setProgressBar(isLoading: Boolean) {
        if (isLoading) {
            searchingProgressBar.visibility = View.VISIBLE
            errorLayout.visibility = View.INVISIBLE
        } else if (!isLoading && searchViewModel.currentState.isError) {
            searchingProgressBar.visibility = View.INVISIBLE
            errorLayout.visibility = View.VISIBLE
        } else {
            searchingProgressBar.visibility = View.INVISIBLE
            errorLayout.visibility = View.INVISIBLE
        }
    }

    private fun loadingMore(isLoadingMore: Boolean) {
        if (isLoadingMore) {
            loadMoreProgressBar.visibility = View.VISIBLE
        } else {
            loadMoreProgressBar.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
        (imm as? InputMethodManager)?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun clearEditText() {
        searchEditText.text.clear()
    }

    private fun goToMovieDetailPage(movieId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

}