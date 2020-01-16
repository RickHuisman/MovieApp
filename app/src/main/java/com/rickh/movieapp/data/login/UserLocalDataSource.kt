package com.rickh.movieapp.data.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class UserLocalDataSource(context: Context) {

    val loggedInUserPrefs: SharedPreferences =
        context.getSharedPreferences(LOGGED_IN_USER_PREFS, MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: UserLocalDataSource? = null

        fun getInstance(context: Context): UserLocalDataSource {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: UserLocalDataSource(
                            context
                        ).also { INSTANCE = it }
                }
        }

        private const val LOGGED_IN_USER_PREFS = "loggedInUserPrefs"
    }
}

