package com.rickh.movieapp.utils

class ColorUtils {

    companion object {
        private fun modifyAlpha(color: Int, alpha: Int): Int {
            return color and 0x00ffffff or (alpha shl 24)
        }

        fun modifyAlpha(color: Int, alpha: Float): Int {
            return modifyAlpha(color, (255f * alpha).toInt())
        }
    }
}