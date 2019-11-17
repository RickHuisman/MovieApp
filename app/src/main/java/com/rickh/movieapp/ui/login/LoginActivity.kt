package com.rickh.movieapp.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
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

        viewModel.uiState.observe(this, Observer {
            val uiModel = it ?: return@Observer

            showProgress(uiModel.showProgress)
            if (uiModel.showError) showLoginFailed()
            if (uiModel.showSuccess) {
                showLoginSuccess()
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        login.setOnClickListener {
            viewModel.login(username.toString(), password.toString())
        }
        sign_up.setOnClickListener {
            signUp()
        }
        frame.setOnClickListener {
            dismiss()
        }
    }

    private fun signUp() {
        val signUpUrl = "https://www.themoviedb.org/account/signup"
        startActivity(Intent(Intent.ACTION_VIEW, signUpUrl.toUri()))
    }

    private fun dismiss() {
        finishAfterTransition()
    }

    private fun showProgress(show: Boolean) {
        progress_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showLoginFailed() {
        username_label.error = getString(R.string.login_failed)
        password_label.error = getString(R.string.login_failed)
        username_label.requestFocus()
    }

    private fun showLoginSuccess() {
        // TODO remove this toast and open profile sheet after successful login
        Toast(applicationContext).apply {
            setText("Login successful")
            duration = Toast.LENGTH_LONG
        }.show()
    }
}