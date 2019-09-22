package com.rickh.movieapp.tmdb

import com.rickh.movieapp.tmdb.models.Feed


object MoviesRepository {

    private val tmdbApi = TmdbRepository.retrofitService

    suspend fun getPopular(pageIndex: Int): Feed {
        return tmdbApi.getPopularMovies(pageIndex)
    }

    suspend fun getTopRated(pageIndex: Int): Feed {
        return tmdbApi.getTopRatedMovies(pageIndex)
    }

    suspend fun getUpcoming(pageIndex: Int): Feed {
        return tmdbApi.getUpcomingMovies(pageIndex)
    }

    suspend fun getNowPlaying(pageIndex: Int): Feed {
        return tmdbApi.getNowPlayingMovies(pageIndex)
    }
}