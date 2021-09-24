package com.congtv5.smartmovie.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.data.model.genre.Genre
import com.congtv5.smartmovie.databinding.GenreItemLayoutBinding
import com.congtv5.smartmovie.ui.view.adapter.diffutil.GenreDiffUtil
import com.congtv5.smartmovie.utils.Constants

class GenreListAdapter(context: Context) :
    ListAdapter<Genre, GenreListAdapter.GenreViewHolder>(GenreDiffUtil()) {

    private val glide = Glide.with(context)

    inner class GenreViewHolder(private val binding: GenreItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.tvGenreTitle.text = genre.name

//            val imageUrl = Constants.IMAGE_BASE_URL + genre.poster_path
            glide.load(R.drawable.test_genre_image)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(binding.ivGenreImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding =
            GenreItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}