package com.rickh.movieapp.ui.moviedetail

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.Target
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.omertron.themoviedbapi.model.credits.MediaCreditCast
import com.rickh.movieapp.R

class CastCreditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val creditImage: ImageView = itemView.findViewById(R.id.credit_image)
    private val title: TextView = itemView.findViewById(R.id.title_textview)
    private val byline: TextView = itemView.findViewById(R.id.byline_textview)


    fun bind(credit: MediaCreditCast) {
        title.text = credit.name
        byline.text = credit.character

        if (credit.artworkPath != null)
            bindProfileImage(credit.artworkPath)
    }

    private fun bindProfileImage(artworkPath: String) {
        Glide.with(creditImage)
            .load("https://image.tmdb.org/t/p/original$artworkPath")
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
                    creditImage.foreground = ContextCompat.getDrawable(
                        creditImage.context,
                        R.drawable.touchindicator_person_thumbnail
                    )
                    return false
                }
            })
            .placeholder(R.drawable.round_placeholder)
            .centerCrop()
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(creditImage)
    }
}