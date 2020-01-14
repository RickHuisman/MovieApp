package com.rickh.movieapp.ui.login

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.rickh.movieapp.tmdb.Result
import timber.log.Timber

class LoginRemoteDataSource {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val requestToken = tmdbApi.authorisationToken
            val validatedRequestToken =
                tmdbApi.getSessionTokenLogin(requestToken, username, password)
            val tokenSession = tmdbApi.getSessionToken(validatedRequestToken)

            return Result.Success(getLoggedInUser(tokenSession.sessionId))
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    private fun getLoggedInUser(sessionId: String): LoggedInUser {
        try {
            val user = tmdbApi.getAccount(sessionId)

            return LoggedInUser(
                sessionId,
                user.id,
                user.name,
                user.userName,
                user.isIncludeAdult,
                user.language,
                user.country,
                user.avatar.hash
            )
        } catch (e: MovieDbException) {
            throw e
        }
    }

    fun logout() {
        // TODO Delete session id from tmdb
    }
}