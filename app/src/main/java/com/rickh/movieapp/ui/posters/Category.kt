package com.rickh.movieapp.ui.posters

import com.rickh.movieapp.R

enum class Category(
    private val titleRes: Int,
    private val iconRes: Int
) {
    MOVIES(R.string.category_movies, R.drawable.ic_movie_18dp),
    TV_SHOWS(R.string.category_tv_shows, R.drawable.ic_tv_18dp),
    DISCOVER(R.string.category_discover, R.drawable.ic_explore_18dp),
    POPULAR_PEOPLE(R.string.category_popular_people, R.drawable.ic_people_18dp);

    fun titleRes(): Int {
        return titleRes
    }

    fun iconRes(): Int {
        return iconRes
    }

    companion object {
        var ALL = arrayOf(
            MOVIES,
            TV_SHOWS,
            DISCOVER,
            POPULAR_PEOPLE
        )
    }
}