package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.authentication.TokenSession

object LoginRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun login(username: String, password: String): Result<TokenSession> {
        return try {
            val requestToken = tmdbApi.authorisationToken
            val validatedRequestToken = tmdbApi.getSessionTokenLogin(requestToken, username, password)
            return Result.Success(tmdbApi.getSessionToken(validatedRequestToken))
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}