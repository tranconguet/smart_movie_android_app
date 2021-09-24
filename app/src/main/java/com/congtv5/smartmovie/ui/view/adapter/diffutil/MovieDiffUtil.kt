package com.congtv5.smartmovie.ui.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.congtv5.smartmovie.data.model.pageresult.Result

class MovieDiffUtil : DiffUtil.ItemCallback<Result>() {

    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }

}