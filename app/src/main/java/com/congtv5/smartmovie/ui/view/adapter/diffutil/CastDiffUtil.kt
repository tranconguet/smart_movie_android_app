package com.congtv5.smartmovie.ui.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.congtv5.smartmovie.data.model.castandcrewlist.Cast

class CastDiffUtil : DiffUtil.ItemCallback<Cast>() {
    override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem.cast_id == newItem.cast_id
    }

    override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem == newItem
    }

}