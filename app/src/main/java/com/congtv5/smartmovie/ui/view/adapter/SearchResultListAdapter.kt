package com.congtv5.smartmovie.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.domain.model.Genre
import com.congtv5.domain.model.Movie
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.view.adapter.diffutil.MovieDiffUtil
import com.congtv5.smartmovie.utils.Constants.IMAGE_BASE_URL
import com.congtv5.smartmovie.utils.formatGenresToString

class SearchResultListAdapter(
    private val onMovieClick: (Int) -> Unit,
    private val getGenreNameById: (Int) -> String?
) : ListAdapter<Movie, SearchResultListAdapter.SearchResultViewHolder>(MovieDiffUtil()) {

    class SearchResultViewHolder(
        view: View,
        private var onMovieClick: (Int) -> Unit,
        private var getGenreNameById: (Int) -> String?
    ) : RecyclerView.ViewHolder(view) {

        private var movie: Movie? = null
        private val searchResultsLayout = view.findViewById<ConstraintLayout>(R.id.searchResultItemLayout)
        private val movieNameTextView = view.findViewById<TextView>(R.id.tvGenreItemTitle)
        private val movieImageView = view.findViewById<ImageView>(R.id.ivGenreItemImage)
        private val ratingBar = view.findViewById<RatingBar>(R.id.rbRatingBar)
        private val genresTextView = view.findViewById<TextView>(R.id.tvGenres)

        init {
            searchResultsLayout.setOnClickListener {
                if (movie != null) {
                    onMovieClick.invoke(movie!!.id)
                } else {
                    //handle null case
                    Log.d("CongTV5", "SearchResultViewHolder #init() movie is null")
                }
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie
            movieNameTextView.text = movie.title
//            tvGenres.text = formatGenresToString(movie.genre_ids)

            val genres = movie.genreIds.filter { id ->
                getGenreNameById(id) != null
            }.map { id ->
                Genre(id, getGenreNameById(id)!!, null)
            }

            genresTextView.text = formatGenresToString(genres)

            val rating = movie.voteAverage.toFloat() / 2
            ratingBar.rating = rating

            val imageUrl = IMAGE_BASE_URL + movie.backdropPath
            Glide.with(movieImageView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(movieImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_result_item_layout, parent, false)
        return SearchResultViewHolder(view, onMovieClick, getGenreNameById)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



}