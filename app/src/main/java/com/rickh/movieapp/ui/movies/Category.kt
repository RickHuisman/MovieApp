package com.rickh.movieapp.ui.movies

import com.rickh.movieapp.R


enum class Category(
    private val titleRes: Int
) {
    MOVIES(R.string.category_tab_movies),
    TV_SHOWS(R.string.category_tab_tv_shows),
    DISCOVER(R.string.category_tab_discover),
    POPULAR_PEOPLE(R.string.category_tab_popular_people);

    companion object {
        var ALL = arrayOf(MOVIES, TV_SHOWS, DISCOVER, POPULAR_PEOPLE)
    }
}