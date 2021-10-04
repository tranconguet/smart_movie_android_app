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
import com.congtv5.smartmovie.utils.Constants
import com.congtv5.smartmovie.utils.Constants.IMAGE_BASE_URL

class SearchResultListAdapter(
    private var onMovieClick: (Int) -> Unit,
    private var getGenreNameById: (Int) -> String?
) : ListAdapter<Movie, SearchResultListAdapter.SearchResultViewHolder>(MovieDiffUtil()) {

    class SearchResultViewHolder(
        view: View,
        private var onMovieClick: (Int) -> Unit,
        private var getGenreNameById: (Int) -> String?
    ) : RecyclerView.ViewHolder(view) {

        private var movie: Movie? = null
        private val layoutSearchResultItem =
            view.findViewById<ConstraintLayout>(R.id.searchResultItemLayout)
        private val tvMovieName = view.findViewById<TextView>(R.id.tvGenreItemTitle)
        private val ivMovieImage = view.findViewById<ImageView>(R.id.ivGenreItemImage)
        private val rbRatingBar = view.findViewById<RatingBar>(R.id.rbRatingBar)
        private val tvGenres = view.findViewById<TextView>(R.id.tvGenres)

        init {
            layoutSearchResultItem.setOnClickListener {
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
            tvMovieName.text = movie.title
//            tvGenres.text = formatGenresToString(movie.genre_ids)

            val genres = movie.genreIds.filter { id ->
                getGenreNameById(id) != null
            }.map { id ->
                Genre(id, getGenreNameById(id)!!, null)
            }

            tvGenres.text = formatGenresToString(genres)

            val rating = movie.voteAverage.toFloat() / 2
            rbRatingBar.rating = rating

            val imageUrl = IMAGE_BASE_URL + movie.backdropPath
            Glide.with(ivMovieImage)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivMovieImage)
        }

        private fun formatGenresToString(genres: List<Genre>): String {
            return if (genres.isNotEmpty()) {
                genres.joinToString("") { genre ->
                    "${genre.name} | "
                }.dropLast(2)
            } else {
                Constants.EMPTY_TEXT
            }
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