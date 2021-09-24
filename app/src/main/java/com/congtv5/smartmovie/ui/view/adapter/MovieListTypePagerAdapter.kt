package com.congtv5.smartmovie.ui.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.congtv5.smartmovie.ui.view.fragments.home.*
import com.congtv5.smartmovie.ui.view.fragments.home.TopRatedMovieFragment
import com.congtv5.smartmovie.utils.Constants.MOVIE_LIST_TYPE

class MovieListTypePagerAdapter(fragment: Fragment, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}