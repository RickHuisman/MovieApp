package com.rickh.movieapp.ui.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SessionTokenLocalDataSource(context: Context) {

    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences(SESSION_PREFS, MODE_PRIVATE)

    fun getSessionPrefs(): SharedPreferences {
        return sessionPrefs
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionTokenLocalDataSource? = null

        fun getInstance(context: Context): SessionTokenLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionTokenLocalDataSource(context).also { INSTANCE = it }
            }
        }

        private const val SESSION_PREFS = "sessionPrefs"
    }
}

