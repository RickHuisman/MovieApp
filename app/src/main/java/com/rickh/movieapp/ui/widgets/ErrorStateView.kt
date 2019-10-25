package com.rickh.movieapp.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.rickh.movieapp.R
import kotlinx.android.synthetic.main.error_state.view.*

class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.error_state, this, true)

        orientation = VERTICAL
        gravity = Gravity.CENTER
    }

    fun setOnRetryClickListener(listener: OnClickListener) {
        retry.setOnClickListener(listener)
    }
}