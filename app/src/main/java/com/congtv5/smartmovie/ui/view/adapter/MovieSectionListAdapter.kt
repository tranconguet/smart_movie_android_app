package com.congtv5.smartmovie.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.congtv5.smartmovie.data.model.ui.MovieSection
import com.congtv5.smartmovie.databinding.MovieSectionLayoutBinding
import com.congtv5.smartmovie.ui.view.adapter.diffutil.MovieSectionDiffUtil
import com.congtv5.smartmovie.utils.MovieItemDisplayType

class MovieSectionListAdapter(
    private val displayType: MovieItemDisplayType,
    private val context: Context,
    private val onMovieClick: ((Int) -> Unit)
) : ListAdapter<MovieSection, MovieSectionListAdapter.MovieSectionViewHolder>(MovieSectionDiffUtil()) {

    inner class MovieSectionViewHolder(private val binding: MovieSectionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var movieListAdapter: MovieListAdapter? = null

        init {
            movieListAdapter = MovieListAdapter(displayType, context, onMovieClick)
            binding.rvMovieList.adapter = movieListAdapter
            binding.rvMovieList.layoutManager = when (displayType) {
                MovieItemDisplayType.GRID -> GridLayoutManager(context, 2)
                MovieItemDisplayType.VERTICAL_LINEAR -> LinearLayoutManager(context)
            }
        }

        fun bind(movieSection: MovieSection) {
            binding.tvMovieSection.text = movieSection.sectionType.text
            movieListAdapter?.submitList(movieSection.movieList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSectionViewHolder {
        val binding =
            MovieSectionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieSectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieSectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}