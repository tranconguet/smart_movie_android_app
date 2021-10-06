package com.congtv5.smartmovie.ui.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.view.adapter.diffutil.MovieSectionDiffUtil
import com.congtv5.smartmovie.ui.view.fragments.home.HomeFragment.Companion.GRID_ITEM_PER_ROW
import com.congtv5.smartmovie.ui.view.model.MovieSection
import com.congtv5.smartmovie.utils.MovieCategory
import com.congtv5.smartmovie.utils.MovieItemDisplayType

class MovieSectionListAdapter(
    private val displayType: MovieItemDisplayType,
    private val onMovieClick: ((Int) -> Unit),
    private val onSectionTitleClick: ((MovieCategory) -> Unit),
    private val onStarClick: (FavoriteMovie) -> Unit
) : ListAdapter<MovieSection, MovieSectionListAdapter.MovieSectionViewHolder>(MovieSectionDiffUtil()) {

    class MovieSectionViewHolder(
        view: View,
        context: Context,
        displayType: MovieItemDisplayType,
        onMovieClick: ((Int) -> Unit),
        onSectionTitleClick: ((MovieCategory) -> Unit),
        onStarClick: (FavoriteMovie) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private var movieSection: MovieSection? = null
        private var movieListAdapter: MovieListAdapter? = null
        private val movieListRecyclerView = view.findViewById<RecyclerView>(R.id.rvMovieList)
        private val movieSectionTitleTextView = view.findViewById<TextView>(R.id.tvMovieSection)
        private val seeAllTextView = view.findViewById<TextView>(R.id.tvSeeAll)

        init {
            movieListAdapter = MovieListAdapter(displayType, onMovieClick, onStarClick)
            movieListRecyclerView.adapter = movieListAdapter
            movieListRecyclerView.layoutManager = when (displayType) {
                MovieItemDisplayType.GRID -> GridLayoutManager(context, GRID_ITEM_PER_ROW)
                MovieItemDisplayType.VERTICAL_LINEAR -> LinearLayoutManager(context)
            }
            seeAllTextView.setOnClickListener {
                if (movieSection != null) {
                    onSectionTitleClick.invoke(movieSection!!.sectionType)
                } else {
                    Log.e("CongTV5", "MovieSectionViewHolder #init movieSection is null")
                }
            }
        }

        fun bind(movieSection: MovieSection) {
            this.movieSection = movieSection
            movieSectionTitleTextView.text = movieSection.sectionType.text
            movieListAdapter?.submitList(movieSection.movieList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_section_layout, parent, false)
        return MovieSectionViewHolder(
            view,
            parent.context,
            displayType,
            onMovieClick,
            onSectionTitleClick,
            onStarClick
        )
    }

    override fun onBindViewHolder(holder: MovieSectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}