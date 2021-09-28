package com.congtv5.smartmovie.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.model.movie.Genre
import com.congtv5.smartmovie.ui.view.adapter.diffutil.GenreDiffUtil

class GenreListAdapter :
    ListAdapter<Genre, GenreListAdapter.GenreViewHolder>(GenreDiffUtil()) {

    class GenreViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val tvGenreTitle = view.findViewById<TextView>(R.id.tvSearchItemTitle)
        private val ivGenreImage = view.findViewById<ImageView>(R.id.ivSearchItemImage)

        fun bind(genre: Genre) {

            tvGenreTitle.text = genre.name

            Glide.with(ivGenreImage)
                .load(R.drawable.test_genre_image)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivGenreImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item_layout, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}