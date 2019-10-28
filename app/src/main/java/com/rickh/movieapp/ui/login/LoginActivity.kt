package com.rickh.movieapp.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rickh.movieapp.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Activity for logging in to themoviedb account
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpToolbar()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        toolbar.background = null
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}