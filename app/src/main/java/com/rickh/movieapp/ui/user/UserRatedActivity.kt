package com.rickh.movieapp.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rickh.movieapp.R
import com.rickh.movieapp.data.login.LoginRepository
import timber.log.Timber

class UserRatedActivity : AppCompatActivity() {

    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_rated)

        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)

        val user = LoginRepository.getInstance(this).user

        user?.let {
            userProfileViewModel.getRatedMovies(it).observe(this, Observer { movies ->
                Timber.d("$movies")
            })
        }
    }
}