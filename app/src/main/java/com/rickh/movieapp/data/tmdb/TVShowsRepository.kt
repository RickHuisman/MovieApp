package com.rickh.movieapp.data.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.data.tmdb.TheMovieDbApi.LANGUAGE

object TVShowsRepository {

    private val tmdbApi = TheMovieDbApi.TheMovieDbApi

    fun getPopular(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVPopular(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getTopRated(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVTopRated(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getOnTheAir(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVOnTheAir(pageIndex, LANGUAGE).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getAiringToday(pageIndex: Int): Result<List<TVInfo>> {
        return try {
            val result = tmdbApi.getTVAiringToday(pageIndex, LANGUAGE, "AD").results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getTvShowInfo(tvShowId: Long): Result<TVInfo> {
        return try {
            val result = tmdbApi.getTVInfo(tvShowId.toInt(), LANGUAGE)
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}