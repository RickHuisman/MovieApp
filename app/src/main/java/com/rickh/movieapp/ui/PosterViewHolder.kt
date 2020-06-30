package com.rickh.movieapp.ui

import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.omertron.themoviedbapi.enumeration.MediaType
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.posters.Category
import com.rickh.movieapp.ui.posters.PosterDetailPopup
import com.rickh.movieapp.ui.posters.PosterItem
import com.rickh.movieapp.ui.posters.PosterTarget
import com.rickh.movieapp.ui.tvshowdetail.TvShowDetailActivity

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

        // TODO Move click listeners
        itemView.setOnLongClickListener {
            test(poster, activity, itemId)

//            val popup = PosterDetailPopup(
//                activity,
//                Category.MOVIES,
//                item.id
//            )
//            popup.showWithAnchor(poster)

            true
        }
        itemView.setOnClickListener {
            if (item.mediaType == MediaType.TV) {
                val intent = TvShowDetailActivity.newIntent(activity, item.id)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, poster, ViewCompat.getTransitionName(poster)!!
                    )

                activity.startActivity(intent, options.toBundle())
            }
        }
    }

    private fun test(view: ImageView, activity: Activity, itemId: Long) {
        Palette.from(view.drawable.toBitmap()).clearFilters().generate { palette ->
            palette?.vibrantSwatch?.rgb?.let {
                val popup = PosterDetailPopup(
                    activity,
                    Category.MOVIES,
                    itemId,
                    "#${Integer.toHexString(it)}"
//                    "#EB962D"
                )

                popup.showWithAnchor(poster)
            }


        }
    }

//    private fun generateTintFromPalette(palette: Palette): Int {
//
//    }

    private fun fade() {
        // TODO
//        poster.setHasTransientState(true)
//        val cm = ObservableColorMatrix()
//        ObjectAnimator.ofFloat(cm, ObservableColorMatrix.SATURATION, 0f, 1f).apply {
//            addUpdateListener {
//                // Setting the saturation overwrites any darkening so need to reapply.
//                // Just animating the color matrix does not invalidate the
//                // drawable so need this update listener.  Also have to create a
//                // new CMCF as the matrix is immutable :(
//                darkenImage(cm)
//            }
//            duration = 2000L
//            interpolator = AnimUtils.getFastOutSlowInInterpolator(poster.context)
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    poster.setHasTransientState(false)
//                }
//            })
//            start()
//        }
    }

    private fun darkenImage(colorMatrix: ColorMatrix = ColorMatrix()) {
        poster.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }
}