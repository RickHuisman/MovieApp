package com.rickh.movieapp.ui.login

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.authentication.TokenSession
import com.rickh.movieapp.tmdb.Result

class LoginRemoteDataSource {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun login(username: String, password: String): Result<TokenSession> {
        return try {
            val requestToken = tmdbApi.authorisationToken
            val validatedRequestToken =
                tmdbApi.getSessionTokenLogin(requestToken, username, password)
            return Result.Success(tmdbApi.getSessionToken(validatedRequestToken))
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun logout() {
        // TODO Delete session id from tmdb
    }
}