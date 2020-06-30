package com.rickh.movieapp.ui.posters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rickh.movieapp.ui.discover.DiscoverFragment
import com.rickh.movieapp.ui.posters.Category.Companion.ALL
import com.rickh.movieapp.ui.people.PopularPeopleFragment
import com.rickh.movieapp.ui.posters.Category
import com.rickh.movieapp.ui.posters.PosterFragment

class CategoryPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (ALL[position]) {
            Category.MOVIES -> PosterFragment.create(
                Category.MOVIES)
            Category.TV_SHOWS -> PosterFragment.create(
                Category.TV_SHOWS)
            Category.DISCOVER -> DiscoverFragment()
            Category.POPULAR_PEOPLE -> PopularPeopleFragment()
        }
    }

    override fun getCount(): Int {
        return ALL.size
    }
}