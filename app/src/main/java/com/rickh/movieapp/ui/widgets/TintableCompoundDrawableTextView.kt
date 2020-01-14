package com.rickh.movieapp.ui.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

class TintableCompoundDrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        applyColorTintToCompoundDrawables(currentTextColor)
    }

    override fun setCompoundDrawablesRelativeWithIntrinsicBounds(
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?
    ) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
        applyColorTintToCompoundDrawables(currentTextColor)
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        applyColorTintToCompoundDrawables(color)
    }

    private fun applyColorTintToCompoundDrawables(@ColorInt tintColor: Int) {
        val drawables = compoundDrawablesRelative

        for (i in drawables.indices) {
            if (drawables[i] != null) {
                drawables[i] = drawables[i].mutate()
                drawables[i].setTint(tintColor)
            }
        }

        super.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0],
            drawables[1],
            drawables[2],
            drawables[3]
        )
    }
}