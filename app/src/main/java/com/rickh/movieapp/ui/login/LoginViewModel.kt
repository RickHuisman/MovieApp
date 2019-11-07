package com.rickh.movieapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.omertron.themoviedbapi.model.authentication.TokenSession
import com.rickh.movieapp.tmdb.AuthenticationRepository
import com.rickh.movieapp.tmdb.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class LoginViewModel : ViewModel() {

    private val authRepository = AuthenticationRepository

    fun login(username: String, password: String): LiveData<Result<TokenSession>> =
        liveData(Dispatchers.IO) {
            emit(authRepository.login(username, password))
        }
}