package com.rickh.movieapp.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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

    private val uiStateObserver = Observer<LoginUiModel> { uiModel ->
        showProgress(uiModel.showProgress)
        if (uiModel.showError) showLoginFailed()
        if (uiModel.showSuccess) {
            setResult(Activity.RESULT_OK)
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        viewModel.uiState.observe(this, uiStateObserver)

        frame.setOnClickListener { dismiss() }
        sign_up.setOnClickListener { signUp() }
        login.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()
            viewModel.login(username, password)
        }
    }

    private fun signUp() {
        startActivity(Intent(Intent.ACTION_VIEW, SIGN_UP_URL.toUri()))
    }

    private fun showProgress(show: Boolean) {
        progress_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showLoginFailed() {
        username_label.error = getString(R.string.login_failed)
        password_label.error = getString(R.string.login_failed)
        username_label.requestFocus()
    }

    override fun onBackPressed() = dismiss()

    private fun dismiss() {
        finishAfterTransition()
    }

    companion object {
        private const val SIGN_UP_URL = "https://www.themoviedb.org/account/signup"
    }
}