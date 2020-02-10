package com.rickh.movieapp.data.tmdb

import com.omertron.themoviedbapi.TheMovieDbApi

object TheMovieDbApi {
    private const val API_KEY = "ea75e60dfc33c0ff92d6e331c9debbea"
    const val LANGUAGE = "en"

    val api by lazy {
        TheMovieDbApi(API_KEY)
    }
}