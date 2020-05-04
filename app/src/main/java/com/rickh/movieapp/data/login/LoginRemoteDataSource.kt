package com.rickh.movieapp.data.login

import com.omertron.themoviedbapi.MovieDbException
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.data.tmdb.TheMovieDbApi

class LoginRemoteDataSource {

    private val tmdbApi = TheMovieDbApi.TheMovieDbApi

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