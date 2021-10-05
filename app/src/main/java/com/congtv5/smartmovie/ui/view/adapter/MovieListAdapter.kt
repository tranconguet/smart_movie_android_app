package com.congtv5.smartmovie.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.domain.model.FavoriteMovie
import com.congtv5.domain.model.Movie
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.view.adapter.diffutil.MovieDiffUtil
import com.congtv5.smartmovie.utils.Constants.EMPTY_TEXT
import com.congtv5.smartmovie.utils.Constants.IMAGE_BASE_URL
import com.congtv5.smartmovie.utils.MovieItemDisplayType

class MovieListAdapter(
    private val displayType: MovieItemDisplayType,
    private var onMovieClick: (Int) -> Unit,
    private var onStarClick: (FavoriteMovie) -> Unit,
    private var isMovieFavorite: (Int) -> Boolean
) : ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffUtil()) {

    class MovieGridItemViewHolder(
        view: View,
        private var onMovieClick: (Int) -> Unit,
        private var onStarClick: (FavoriteMovie) -> Unit,
        private var isMovieFavorite: (Int) -> Boolean
    ) : RecyclerView.ViewHolder(view) {

        private var movie: Movie? = null
        private val layoutMovie = view.findViewById<ConstraintLayout>(R.id.layoutMovie)
        private val tvMovieName = view.findViewById<TextView>(R.id.tvMovieName)
        private val ivMovieImage = view.findViewById<ImageView>(R.id.ivMovieImage)
        private val ivStar = view.findViewById<ImageView>(R.id.ivStar)
        private val tvMovieRunTime = view.findViewById<TextView>(R.id.tvMovieRunTime)

        init {
            layoutMovie.setOnClickListener {
                if (movie != null) {
                    onMovieClick.invoke(movie!!.id)
                } else {
                    //handle null case
                    Log.d("CongTV5", "MovieGridItemViewHolder #init() movie is null")
                }
            }

            ivStar.setOnClickListener {
                if (movie != null) {
                    val isFav = isMovieFavorite.invoke(movie!!.id) // check if movie favorite
                    if (!isFav) { // set icon
                        ivStar.setImageResource(R.drawable.star_active)
                    } else {
                        ivStar.setImageResource(R.drawable.star_default)
                    }
                    val favMovie = FavoriteMovie(movie!!.id, !isFav)
                    onStarClick.invoke(favMovie)
                } else {
                    //handle null case
                    Log.d("CongTV5", "MovieGridItemViewHolder #init() movie is null")
                }
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie

            tvMovieName.text = movie.title
            tvMovieRunTime.text = formatTimeToString(movie.runtime)

            val isFav = isMovieFavorite.invoke(movie.id)
            if (isFav) {
                ivStar.setImageResource(R.drawable.star_active)
            } else {
                ivStar.setImageResource(R.drawable.star_default)
            }

            val imageUrl = IMAGE_BASE_URL + movie.posterPath
            Glide.with(ivMovieImage)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivMovieImage)
        }

        private fun formatTimeToString(runtime: Int): String {
            val hours = runtime / 60
            val minutes = runtime - hours * 60
            return when {
                hours > 0 -> "${hours}h ${minutes}m"
                hours == 0 -> "${minutes}m"
                else -> EMPTY_TEXT // api result fail
            }
        }
    }

    class MovieLinearItemViewHolder(
        view: View,
        private var onMovieClick: (Int) -> Unit,
        private var onStarClick: (FavoriteMovie) -> Unit,
        private var isMovieFavorite: (Int) -> Boolean
    ) : RecyclerView.ViewHolder(view) {

        private var movie: Movie? = null
        private val layoutMovie = view.findViewById<ConstraintLayout>(R.id.layoutMovie)
        private val tvMovieName = view.findViewById<TextView>(R.id.tvMovieName)
        private val tvMovieDescription = view.findViewById<TextView>(R.id.tvMovieDescription)
        private val ivMovieImage = view.findViewById<ImageView>(R.id.ivMovieImage)
        private val ivStar = view.findViewById<ImageView>(R.id.ivStar)

        init {
            layoutMovie.setOnClickListener {
                if (movie != null) {
                    onMovieClick.invoke(movie!!.id)
                } else {
                    //handle null case
                    Log.d("CongTV5", "MovieLinearItemViewHolder #init() movie is null")
                }
            }

            ivStar.setOnClickListener {
                if (movie != null) {
                    val isFav = isMovieFavorite.invoke(movie!!.id) // check if movie favorite
                    //set icon
                    if (!isFav) {
                        ivStar.setImageResource(R.drawable.star_active)
                    } else {
                        ivStar.setImageResource(R.drawable.star_default)
                    }
                    val favMovie = FavoriteMovie(movie!!.id, !isFav)
                    onStarClick.invoke(favMovie)
                } else {
                    //handle null case
                    Log.d("CongTV5", "MovieGridItemViewHolder #init() movie is null")
                }
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie

            val isFav = isMovieFavorite.invoke(movie.id)
            if (isFav) {
                ivStar.setImageResource(R.drawable.star_active)
            } else {
                ivStar.setImageResource(R.drawable.star_default)
            }

            tvMovieName.text = movie.title
            tvMovieDescription.text = movie.overview

            val imageUrl = IMAGE_BASE_URL + movie.posterPath
            Glide.with(ivMovieImage)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivMovieImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (displayType) {
            MovieItemDisplayType.GRID -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.grid_display_movie_item_layout, parent, false)
                MovieGridItemViewHolder(view, onMovieClick, onStarClick, isMovieFavorite)
            }
            MovieItemDisplayType.VERTICAL_LINEAR -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.linear_display_movie_item_layout, parent, false)
                MovieLinearItemViewHolder(view, onMovieClick, onStarClick, isMovieFavorite)
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