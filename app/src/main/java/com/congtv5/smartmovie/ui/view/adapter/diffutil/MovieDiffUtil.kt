package com.congtv5.smartmovie.ui.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.congtv5.domain.model.Movie

class MovieDiffUtil : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}