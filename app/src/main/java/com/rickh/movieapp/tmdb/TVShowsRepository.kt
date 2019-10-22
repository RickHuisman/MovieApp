package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.tv.TVInfo

object TVShowsRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPopular(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVPopular(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getTopRated(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVTopRated(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getOnTheAir(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVOnTheAir(pageIndex, "en").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getAiringToday(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVAiringToday(pageIndex, "en", "AD").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}