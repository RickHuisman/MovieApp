package com.rickh.movieapp.tmdb

import android.content.Context
import com.omertron.themoviedbapi.model.authentication.TokenSession
import com.rickh.movieapp.ui.login.LoginLocalDataSource
import com.rickh.movieapp.ui.login.LoginRemoteDataSource
import com.rickh.movieapp.ui.login.SessionTokenLocalDataSource

class LoginRepository(context: Context) {

    private val localDataSource = LoginLocalDataSource(
        SessionTokenLocalDataSource.getInstance(context)
    )
    private val remoteDataSource = LoginRemoteDataSource()

    var sessionId: String? = null
        private set

    init {
        sessionId = localDataSource.getSessionId()
    }

    fun logout() {
        sessionId = null

        localDataSource.logout()
        remoteDataSource.logout()
    }

    fun login(username: String, password: String): Result<TokenSession> {
        val result = remoteDataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    private fun setLoggedInUser(tokenSession: TokenSession) {
        sessionId = tokenSession.sessionId
        localDataSource.setSessionId(tokenSession.sessionId)
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginRepository? = null

        fun getInstance(context: Context): LoginRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginRepository(context).also { INSTANCE = it }
            }
        }
    }
}