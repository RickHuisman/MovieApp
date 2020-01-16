package com.rickh.movieapp.data.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.model.movie.MovieBasic
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.results.ResultList
import com.rickh.movieapp.data.login.LoggedInUser

object UserProfileRepository {

    private val tmdbApi = TheMovieDbApi.api

    fun getRatedMovies(user: LoggedInUser): Result<ResultList<MovieBasic>> {
        return try {
            val result = tmdbApi.getRatedMovies(user.sessionId, user.id, 1, "created_at.asc", "en")
            return Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getRatedTV(user: LoggedInUser): Result<ResultList<TVBasic>> {
        return try {
            val result = tmdbApi.getRatedTV(user.sessionId, user.id, 1, "created_at.asc", "en")
            return Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}