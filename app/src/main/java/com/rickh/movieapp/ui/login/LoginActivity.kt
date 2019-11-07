package com.rickh.movieapp.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.rickh.movieapp.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Activity for logging in to themoviedb account
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        login.setOnClickListener {
            viewModel.login(username.toString(), password.toString())
        }
        sign_up.setOnClickListener {

        }
        frame.setOnClickListener {
            dismiss()
        }
    }

    private fun dismiss() {
        finishAfterTransition()
    }
}