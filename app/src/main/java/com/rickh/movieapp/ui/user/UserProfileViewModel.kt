package com.rickh.movieapp.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rickh.movieapp.data.login.LoggedInUser
import com.rickh.movieapp.data.tmdb.UserProfileRepository
import kotlinx.coroutines.Dispatchers

class UserProfileViewModel : ViewModel() {

    private val userProfileRepository = UserProfileRepository

    fun getRatedMovies(user: LoggedInUser) = liveData(Dispatchers.IO) {
        emit(userProfileRepository.getRatedMovies(user))
    }
}