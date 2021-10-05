package com.congtv5.smartmovie.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congtv5.domain.model.Genre
import com.congtv5.smartmovie.R
import com.congtv5.smartmovie.ui.view.adapter.diffutil.GenreDiffUtil
import com.congtv5.smartmovie.utils.Constants

class GenreListAdapter(
    private val onClickGenre: (Int, String) -> Unit
) : ListAdapter<Genre, GenreListAdapter.GenreViewHolder>(GenreDiffUtil()) {

    class GenreViewHolder(
        view: View, private val onClickGenre: (Int, String) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        var genre: Genre? = null

        private val tvGenreTitle = view.findViewById<TextView>(R.id.tvGenreItemTitle)
        private val ivGenreImage = view.findViewById<ImageView>(R.id.ivGenreItemImage)
        private val layoutGenreItem = view.findViewById<ConstraintLayout>(R.id.layoutGenreItem)

        init {
            layoutGenreItem.setOnClickListener {
                if (genre != null){
                    onClickGenre.invoke(genre!!.id, genre!!.name)
                }else{
                    Log.d("CongTV5","GenreViewHolder #init genre value is null")
                }
            }
        }

        fun bind(genre: Genre) {
            this.genre = genre
            tvGenreTitle.text = genre.name

            val imageUrl = Constants.IMAGE_BASE_URL + genre.backdropPath

            Glide.with(ivGenreImage)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(ivGenreImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item_layout, parent, false)
        return GenreViewHolder(view, onClickGenre)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}