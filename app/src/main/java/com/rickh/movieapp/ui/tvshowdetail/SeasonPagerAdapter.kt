package com.rickh.movieapp.ui.tvshowdetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic

class SeasonPagerAdapter(
    private val tvShowId: Int,
    private val seasons: List<TVSeasonBasic>,
    manager: FragmentManager
) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return TvShowSeasonFragment.create(tvShowId, seasons[position].seasonNumber)
    }

    override fun getCount(): Int {
        return seasons.size
    }
}