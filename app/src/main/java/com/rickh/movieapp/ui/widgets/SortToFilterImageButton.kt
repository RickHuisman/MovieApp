package com.rickh.movieapp.ui.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.rickh.movieapp.R


class SortToFilterImageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    private var showingFilter: Boolean = false
    private var iconVisible: Boolean = false
    private var filterToSort: AnimatedVectorDrawableCompat?
    private val sortToFilter: AnimatedVectorDrawableCompat?

    private var sortAppear: AnimatedVectorDrawableCompat?
    private var sortDisappear: AnimatedVectorDrawableCompat?
    private var filterAppear: AnimatedVectorDrawableCompat?
    private var filterDisappear: AnimatedVectorDrawableCompat?

    init {
        showingFilter = false
        iconVisible = true

        filterToSort = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_filter_to_sort)
        sortToFilter = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_sort_to_filter)

        sortAppear = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_sort_appear)
        sortDisappear = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_sort_disappear)
        filterAppear = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_filter_appear)
        filterDisappear = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_filter_disappear)
    }

    fun showSort() {
        if (showingFilter or !iconVisible) {
            val drawable = if (!iconVisible) sortAppear else filterToSort
            startDrawable(drawable)

            showingFilter = false
            iconVisible = true
            visibility = View.VISIBLE
        }
    }

    fun showFilter() {
        if (!showingFilter or !iconVisible) {
            val drawable = if (!iconVisible) filterAppear else sortToFilter
            startDrawable(drawable)

            showingFilter = true
            iconVisible = true
            visibility = View.VISIBLE
        }
    }

    fun disappear() {
        val drawable = if (showingFilter) filterDisappear else sortDisappear
        startDrawable(drawable)
        iconVisible = false

        drawable?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                visibility = View.GONE
            }
        })
    }

    private fun startDrawable(drawable: AnimatedVectorDrawableCompat?) {
        setImageDrawable(drawable)
        drawable?.start()
    }
}