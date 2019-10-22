package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.movie.MovieInfo

object MoviesRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPopular(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getPopularMovieList(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getTopRated(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getTopRatedMovies(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getUpcoming(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getUpcoming(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getNowPlaying(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getNowPlayingMovies(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getMovie(movieId: Long): Result<MovieInfo> {
        return try {
            val result = tmdbApi.getMovieInfo(movieId.toInt(), "en")
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}