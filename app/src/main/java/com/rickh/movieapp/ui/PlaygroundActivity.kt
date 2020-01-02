package com.rickh.movieapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rickh.movieapp.R
import kotlinx.android.synthetic.main.activity_playground.*

class PlaygroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

        haulerView.setOnDragDismissedListener {
            finish()
        }
    }
}