package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.tv.TVInfo

object TVShowsRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPopular(pageIndex: Int): List<TVInfo> {
        return tmdbApi.getTVPopular(pageIndex, "en").results
    }

    fun getTopRated(pageIndex: Int): List<TVInfo> {
        return tmdbApi.getTVTopRated(pageIndex, "en").results
    }

    fun getOnTheAir(pageIndex: Int): List<TVInfo> {
        return tmdbApi.getTVOnTheAir(pageIndex, "en").results
    }

    fun getAiringToday(pageIndex: Int): List<TVInfo> {
        return tmdbApi.getTVAiringToday(pageIndex, "en", "AD").results
    }
}