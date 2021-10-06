package com.congtv5.smartmovie.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.domain.model.Cast
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.view.adapter.diffutil.CastDiffUtil
import com.congtv5.smartmovie.utils.Constants.IMAGE_BASE_URL

class CastListAdapter :
    ListAdapter<Cast, CastListAdapter.CastViewHolder>(CastDiffUtil()) {

    class CastViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val castNameTextView = view.findViewById<TextView>(R.id.tvCastName)
        private val castImageView = view.findViewById<ImageView>(R.id.ivCastImage)

        fun bind(cast: Cast) {
            castNameTextView.text = cast.name

            val imageUrl = IMAGE_BASE_URL + cast.profilePath
            Glide.with(castImageView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(castImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cast_item_layout, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}