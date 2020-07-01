package com.rickh.movieapp.ui.posters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.rickh.movieapp.R
import com.rickh.movieapp.utils.ViewUtils
import timber.log.Timber

/**
 * A Glide [com.bumptech.glide.request.target.ViewTarget]
 * It applies a palette generated ripple.
 */
class PosterTarget(
    private val posterImageView: ImageView
) : DrawableImageViewTarget(posterImageView) {

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        Palette.from(resource.toBitmap()).clearFilters().generate { palette ->
            posterImageView.foreground = ViewUtils.createRipple(
                palette,
                0.25f,
                0.5f,
                ContextCompat.getColor(view.context, R.color.mid_grey),
                true
            )
        }
    }
}