package com.rickh.movieapp.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.util.DisplayMetrics
import androidx.palette.graphics.Palette
import kotlin.math.roundToInt


class ViewUtils {

    companion object {
        fun dpToPix(context: Context, dp: Int): Int {
            return (dp * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }

        fun createRipple(
            palette: Palette?,
            darkAlpha: Float,
            lightAlpha: Float,
            fallbackColor: Int,
            bounded: Boolean
        ): RippleDrawable {
            var rippleColor = fallbackColor

            if (palette != null) {
                when {
                    palette.vibrantSwatch != null -> rippleColor =
                        ColorUtils.modifyAlpha(palette.vibrantSwatch!!.rgb, darkAlpha)
                    palette.lightVibrantSwatch != null -> rippleColor = ColorUtils.modifyAlpha(
                        palette.lightVibrantSwatch!!.rgb,
                        lightAlpha
                    )
                    palette.darkVibrantSwatch != null -> rippleColor = ColorUtils.modifyAlpha(
                        palette.darkVibrantSwatch!!.rgb,
                        darkAlpha
                    )
                    palette.mutedSwatch != null -> rippleColor =
                        ColorUtils.modifyAlpha(palette.mutedSwatch!!.rgb, darkAlpha)
                    palette.lightMutedSwatch != null -> rippleColor = ColorUtils.modifyAlpha(
                        palette.lightMutedSwatch!!.rgb,
                        lightAlpha
                    )
                    palette.darkMutedSwatch != null -> rippleColor =
                        ColorUtils.modifyAlpha(palette.darkMutedSwatch!!.rgb, darkAlpha)
                }
            }
            return RippleDrawable(
                ColorStateList.valueOf(rippleColor), null,
                if (bounded) ColorDrawable(Color.WHITE) else null
            )
        }
    }
}