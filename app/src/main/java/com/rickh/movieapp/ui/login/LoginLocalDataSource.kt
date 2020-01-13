package com.rickh.movieapp.ui.login

import androidx.core.content.edit

class LoginLocalDataSource(
    private val sessionTokenDataSource: SessionTokenLocalDataSource
) {

    fun setSessionId(sessionId: String) {
        sessionTokenDataSource.getSessionPrefs().edit {
            putString(KEY_SESSION_ID, sessionId)
        }
    }

    fun getSessionId(): String? {
        val sessionId = sessionTokenDataSource.getSessionPrefs().getString(KEY_SESSION_ID, "")

        return if (sessionId == "") null else sessionId
    }

    fun logout() {
        sessionTokenDataSource.getSessionPrefs().edit {
            putString(KEY_SESSION_ID, null)
        }
    }

    companion object {
        private const val KEY_SESSION_ID = "KEY_SESSION_ID"
    }
}