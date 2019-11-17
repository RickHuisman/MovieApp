package com.rickh.movieapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickh.movieapp.R
import com.rickh.movieapp.tmdb.LoginRepository
import com.rickh.movieapp.tmdb.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private val loginRepository = LoginRepository

    private var loginJob: Job? = null

    private val _uiState = MutableLiveData<LoginUiModel>()
    val uiState: LiveData<LoginUiModel>
        get() = _uiState

    fun login(username: String, password: String) {
        // only allow one login at a time
        if (loginJob?.isActive == true) {
            return
        }
        loginJob = launchLogin(username, password)
    }

    private fun launchLogin(username: String, password: String): Job {
        return viewModelScope.launch(Dispatchers.Default) {
            if (!isLoginValid(username, password)) {
                return@launch
            }
            withContext(Dispatchers.Main) { showLoading() }
            val result = loginRepository.login(username, password)

            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    emitUiState(
                        showSuccess = true
                    )
                } else {
                    emitUiState(
                        showError = true
                    )
                }
            }
        }
    }

    private fun showLoading() {
        emitUiState(showProgress = true)
    }

    private fun isLoginValid(username: String, password: String): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: Boolean = false,
        showSuccess: Boolean = false
    ) {
        val uiModel = LoginUiModel(showProgress, showError, showSuccess)
        _uiState.value = uiModel
    }
}

/**
 * UI model for [LoginActivity]
 */
data class LoginUiModel(
    val showProgress: Boolean,
    val showError: Boolean,
    val showSuccess: Boolean
)