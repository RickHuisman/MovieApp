package com.rickh.movieapp.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.rickh.movieapp.data.login.LoginRepository
import com.rickh.movieapp.data.tmdb.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(context: Application) : AndroidViewModel(context) {

    private val loginRepository = LoginRepository.getInstance(context)

    private var loginJob: Job? = null

    private val _uiState = MutableLiveData<LoginUiModel>()
    val uiState: LiveData<LoginUiModel>
        get() = _uiState

    fun login(username: String, password: String) {
        // Only allow one login at a time
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
                when (result) {
                    is Result.Success -> showSuccess()
                    is Result.Error -> showError()
                }
            }
        }
    }

    private fun showLoading() {
        emitUiState(showProgress = true)
    }

    private fun showSuccess() {
        emitUiState(showSuccess = true)
    }

    private fun showError() {
        emitUiState(showError = true)
    }

    private fun isLoginValid(username: String, password: String): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: Boolean = false,
        showSuccess: Boolean = false
    ) {
        _uiState.value = LoginUiModel(showProgress, showError, showSuccess)
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