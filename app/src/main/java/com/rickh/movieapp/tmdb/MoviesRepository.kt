package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.movie.MovieInfo

object MoviesRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPopular(pageIndex: Int): List<MovieInfo> {
        return tmdbApi.getPopularMovieList(pageIndex, "en").results
    }

    fun getTopRated(pageIndex: Int): List<MovieInfo> {
        return tmdbApi.getTopRatedMovies(pageIndex, "en").results
    }

    fun getUpcoming(pageIndex: Int): List<MovieInfo> {
        return tmdbApi.getUpcoming(pageIndex, "en").results
    }

    fun getNowPlaying(pageIndex: Int): List<MovieInfo> {
        return tmdbApi.getNowPlayingMovies(pageIndex, "en").results
    }

    fun getMovie(movieId: Long): MovieInfo {
        return tmdbApi.getMovieInfo(movieId.toInt(), "en")
    }
}