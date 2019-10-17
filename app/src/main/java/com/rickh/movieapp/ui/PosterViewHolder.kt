package com.rickh.movieapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.movies.PosterDetailPopup
import com.rickh.movieapp.ui.movies.PosterItem
import com.rickh.movieapp.ui.movies.PosterTarget
import com.rickh.movieapp.utils.AnimUtils
import com.rickh.movieapp.utils.ObservableColorMatrix

class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val poster: ImageView = itemView.findViewById(R.id.poster)

    init {
        darkenImage()
    }

    fun bind(item: PosterItem, placeholder: ColorDrawable, activity: Activity) {
        Glide.with(poster)
            .load(
                poster.context.getString(R.string.tmdb_base_img_url, item.posterPath)
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ) = false

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (!item.hasFadedIn) {
                        fade()
                        item.hasFadedIn = true
                    }
                    return false
                }
            })
            .placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(PosterTarget(poster))

        itemView.setOnLongClickListener {
            val popup = PosterDetailPopup(activity, item.id)
            popup.showWithAnchor(poster)

            true
        }
    }

    private fun fade() {
        poster.setHasTransientState(true)
        val cm = ObservableColorMatrix()
        ObjectAnimator.ofFloat(cm, ObservableColorMatrix.SATURATION, 0f, 1f).apply {
            addUpdateListener {
                // Setting the saturation overwrites any darkening so need to reapply.
                // Just animating the color matrix does not invalidate the
                // drawable so need this update listener.  Also have to create a
                // new CMCF as the matrix is immutable :(
                darkenImage(cm)
            }
            duration = 2000L
            interpolator = AnimUtils.getFastOutSlowInInterpolator(poster.context)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    poster.setHasTransientState(false)
                }
            })
            start()
        }
    }

    private fun darkenImage(colorMatrix: ColorMatrix = ColorMatrix()) {
        poster.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }
}