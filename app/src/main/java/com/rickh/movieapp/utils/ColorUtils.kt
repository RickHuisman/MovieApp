package com.rickh.movieapp.utils

import android.app.Activity
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.rickh.movieapp.ui.posters.Category
import com.rickh.movieapp.ui.posters.PosterDetailPopup

class ColorUtils {

    companion object {
        private fun modifyAlpha(color: Int, alpha: Int): Int {
            return color and 0x00ffffff or (alpha shl 24)
        }

        fun modifyAlpha(color: Int, alpha: Float): Int {
            return modifyAlpha(color, (255f * alpha).toInt())
        }

        fun getImageColorPalette(view: ImageView, activity: Activity, itemId: Long) {
        }
    }
}