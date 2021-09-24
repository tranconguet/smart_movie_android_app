package com.congtv5.smartmovie.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.model.pageresult.Result
import com.congtv5.smartmovie.databinding.GridDisplayMovieItemLayoutBinding
import com.congtv5.smartmovie.databinding.LinearDisplayMovieItemLayoutBinding
import com.congtv5.smartmovie.ui.view.adapter.diffutil.MovieDiffUtil
import com.congtv5.smartmovie.utils.Constants.IMAGE_BASE_URL
import com.congtv5.smartmovie.utils.MovieItemDisplayType

class MovieListAdapter(private val displayType: MovieItemDisplayType, context: Context, private var onMovieClick: (Int) -> Unit) :
    ListAdapter<Result, RecyclerView.ViewHolder>(MovieDiffUtil()) {

    private val glide = Glide.with(context)

    inner class MovieGridItemViewHolder(private val binding: GridDisplayMovieItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var movie: Result? = null

        init {
            binding.layoutMovie.setOnClickListener {
                movie?.let { movie ->
                    onMovieClick.invoke(movie.id)
                }
            }
        }

        fun bind(movie: Result) {
            this.movie = movie
            binding.tvMovieName.text = movie.title
            val imageUrl = IMAGE_BASE_URL + movie.poster_path
            glide.load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(binding.ivMovieImage)
        }

    }

    inner class MovieLinearItemViewHolder(private val binding: LinearDisplayMovieItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var movie: Result? = null

        init {
            binding.layoutMovie.setOnClickListener {
                movie?.let { movie ->
                    onMovieClick.invoke(movie.id)
                }
            }
        }

        fun bind(movie: Result) {
            this.movie = movie
            binding.tvMovieName.text = movie.title
            val imageUrl = IMAGE_BASE_URL + movie.poster_path
            glide.load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(binding.ivMovieImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (displayType) {
            MovieItemDisplayType.GRID -> {
                val binding = GridDisplayMovieItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieGridItemViewHolder(binding)
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                val binding = LinearDisplayMovieItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieLinearItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (displayType) {
            MovieItemDisplayType.GRID -> {
                (holder as? MovieGridItemViewHolder)?.bind(getItem(position))
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                (holder as? MovieLinearItemViewHolder)?.bind(getItem(position))
            }
        }
    }

}