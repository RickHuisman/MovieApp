package com.rickh.movieapp.ui.movies

enum class Category {
    MOVIES,
    TV_SHOWS,
    DISCOVER,
    POPULAR_PEOPLE;

    companion object {
        var ALL = arrayOf(MOVIES, TV_SHOWS, DISCOVER, POPULAR_PEOPLE)
    }
}