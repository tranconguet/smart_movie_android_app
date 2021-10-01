package com.congtv5.smartmovie.ui.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.congtv5.smartmovie.ui.view.model.MovieSection

class MovieSectionDiffUtil : DiffUtil.ItemCallback<MovieSection>() {
    override fun areItemsTheSame(oldItem: MovieSection, newItem: MovieSection): Boolean {
        return oldItem.sectionType == newItem.sectionType
    }

    override fun areContentsTheSame(oldItem: MovieSection, newItem: MovieSection): Boolean {
        return oldItem == newItem
    }
}