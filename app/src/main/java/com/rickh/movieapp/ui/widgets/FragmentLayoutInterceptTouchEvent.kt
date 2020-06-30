package com.rickh.movieapp.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class FragmentLayoutInterceptTouchEvent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var touchEventListener: TouchEventListener? = null

    interface TouchEventListener {
        fun onInterceptTouchEvent()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        touchEventListener?.onInterceptTouchEvent()
        return false
    }
}