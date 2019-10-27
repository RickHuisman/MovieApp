package com.rickh.movieapp.ui.people

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.omertron.themoviedbapi.model.movie.MovieBasic
import com.omertron.themoviedbapi.model.person.PersonFind
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.rickh.movieapp.R

class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val detail: TextView = itemView.findViewById(R.id.detail)

    fun bind(person: PersonFind) {
        Glide.with(profileImage)
            .load("https://image.tmdb.org/t/p/original${person.profilePath}")
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
                    profileImage.foreground = ContextCompat.getDrawable(
                        profileImage.context,
                        R.drawable.touchindicator_person_thumbnail
                    )
                    return false
                }
            })
            .placeholder(R.drawable.round_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .centerCrop()
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(profileImage)
        name.text = person.name

        val builder = StringBuilder()
        var count = 0
        for (media in person.knownFor) {
            if (media is MovieBasic) {
                builder.append(media.originalTitle)
            } else if (media is TVBasic) {
                builder.append(media.originalName)
            }
            if (count < 2) {
                builder.append(" · ")
            }
            count++
        }
        detail.text = builder.toString()
    }
}