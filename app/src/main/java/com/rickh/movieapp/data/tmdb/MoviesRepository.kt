package com.rickh.movieapp.data.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.rickh.movieapp.data.tmdb.TheMovieDbApi.LANGUAGE

object MoviesRepository {

    private val tmdbApi = TheMovieDbApi.api

    fun getPopular(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getPopularMovieList(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getTopRated(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getTopRatedMovies(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getUpcoming(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getUpcoming(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getNowPlaying(pageIndex: Int): Result<List<MovieInfo>> {
        return try {
            val result = tmdbApi.getNowPlayingMovies(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getMovieInfo(movieId: Long): Result<MovieInfo> {
        return try {
            val result = tmdbApi.getMovieInfo(movieId.toInt(), LANGUAGE)
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}