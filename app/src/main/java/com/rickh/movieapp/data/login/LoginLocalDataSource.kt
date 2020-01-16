package com.rickh.movieapp.data.login

import androidx.core.content.edit
import com.squareup.moshi.Moshi

class LoginLocalDataSource(
    private val userDataSource: UserLocalDataSource
) {

    private val moshi = Moshi.Builder().build()
    private val loggedInUserJsonAdapter = moshi.adapter(LoggedInUser::class.java)

    fun setLoggedInUser(loggedInUser: LoggedInUser) {
        val jsonLoggedInUser = loggedInUserJsonAdapter.toJson(loggedInUser)

        userDataSource.loggedInUserPrefs.edit {
            putString("KEY_LOGGED_IN_USER", jsonLoggedInUser)
        }
    }

    fun getLoggedInUser(): LoggedInUser? {
        val jsonLoggedInUser = userDataSource.loggedInUserPrefs.getString(
            KEY_LOGGED_IN_USER, null
        )

        return if (jsonLoggedInUser != null) {
            loggedInUserJsonAdapter.fromJson(jsonLoggedInUser)
        } else null
    }

    fun logout() {
        userDataSource.loggedInUserPrefs.edit {
            putString(KEY_LOGGED_IN_USER, null)
        }
    }

    companion object {
        private const val KEY_LOGGED_IN_USER = "KEY_LOGGED_IN_USER"
    }
}