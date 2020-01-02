package com.rickh.movieapp.ui.people

import android.content.Intent
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
import com.omertron.themoviedbapi.model.media.MediaBasic
import com.omertron.themoviedbapi.model.movie.MovieBasic
import com.omertron.themoviedbapi.model.person.PersonFind
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.PlaygroundActivity
import com.rickh.movieapp.ui.persondetail.PersonDetailActivity

class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val byline: TextView = itemView.findViewById(R.id.byline)

    fun bind(person: PersonFind) {
        name.text = person.name
        byline.text = getKnownForByline(person.knownFor)
        bindProfileImage(person.profilePath)

        val intent = PersonDetailActivity.newIntent(profileImage.context, person)
        itemView.setOnClickListener {
            profileImage.context.startActivity(intent)
        }
        profileImage.setOnClickListener {
//            profileImage.context.startActivity(intent)
            // TODO remove intent
            profileImage.context.startActivity(Intent(profileImage.context, PlaygroundActivity::class.java))
        }
    }

    private fun getKnownForByline(knownFor: List<MediaBasic>): String {
        val builder = StringBuilder()
        for ((count, media) in knownFor.withIndex()) {
            builder.append(
                when (media) {
                    is MovieBasic -> media.originalTitle
                    is TVBasic -> media.originalName
                    else -> "-"
                }
            )

            if (count < (knownFor.size - 1))
                builder.append(" · ")
        }
        return builder.toString()
    }

    private fun bindProfileImage(profilePath: String) {
        Glide.with(profileImage)
            .load("https://image.tmdb.org/t/p/original$profilePath")
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
    }
}