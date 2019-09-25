package com.rickh.movieapp.tmdb

import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.MovieDb


object MoviesRepository {

    private val tmdbApi = TmdbApi("ea75e60dfc33c0ff92d6e331c9debbea").movies

    fun getPopular(pageIndex: Int): List<MovieDb> {
        return tmdbApi.getPopularMovies("en", pageIndex).results
    }

    fun getTopRated(pageIndex: Int): List<MovieDb> {
        return tmdbApi.getTopRatedMovies("en", pageIndex).results
    }

    fun getUpcoming(pageIndex: Int): List<MovieDb> {
        return tmdbApi.getUpcoming("en", pageIndex, "").results
    }

    fun getNowPlaying(pageIndex: Int): List<MovieDb> {
        return tmdbApi.getNowPlayingMovies("en", pageIndex, "").results
    }
}