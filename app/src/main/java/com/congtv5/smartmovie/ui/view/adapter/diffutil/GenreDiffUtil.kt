package com.congtv5.smartmovie.ui.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.congtv5.domain.model.Genre

class GenreDiffUtil : DiffUtil.ItemCallback<Genre>() {

    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }

}