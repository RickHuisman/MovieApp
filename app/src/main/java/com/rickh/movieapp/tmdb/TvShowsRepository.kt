package com.rickh.movieapp.tmdb

import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvSeries


object TvShowsRepository {

    private val tmdbApi = TmdbApi("ea75e60dfc33c0ff92d6e331c9debbea").tvSeries

    fun getPopular(pageIndex: Int): List<TvSeries> {
        return tmdbApi.getPopular("en", pageIndex).results
    }

    fun getTopRated(pageIndex: Int): List<TvSeries> {
        return tmdbApi.getTopRated("en", pageIndex).results
    }

    fun getOnTheAir(pageIndex: Int): List<TvSeries> {
        return tmdbApi.getOnTheAir("en", pageIndex).results
    }

    fun getAiringToday(pageIndex: Int): List<TvSeries> {
        return tmdbApi.getAiringToday("en", pageIndex, null).results
    }
}