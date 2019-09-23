package com.rickh.movieapp.ui.movies

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rickh.movieapp.ui.movies.Category.Companion.ALL


class CategoryPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(
    manager
) {

    override fun getItem(position: Int): Fragment {
        return CategoryFragment().create(ALL[position])
    }

    override fun getCount(): Int {
        return ALL.size
    }
}