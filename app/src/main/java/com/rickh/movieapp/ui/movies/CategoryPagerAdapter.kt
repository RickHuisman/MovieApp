package com.rickh.movieapp.ui.movies

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rickh.movieapp.ui.movies.Category.Companion.ALL
import com.rickh.movieapp.ui.people.PopularPeopleFragment

class CategoryPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (ALL[position]) {
            Category.MOVIES -> CategoryFragment().create(Category.MOVIES)
            Category.TV_SHOWS -> CategoryFragment().create(Category.TV_SHOWS)
            Category.DISCOVER -> TODO()
            Category.POPULAR_PEOPLE -> PopularPeopleFragment()
        }
    }

    override fun getCount(): Int {
        return ALL.size
    }
}