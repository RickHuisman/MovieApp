package com.rickh.movieapp.ui.persondetail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.credits.CreditMovieBasic
import com.omertron.themoviedbapi.model.credits.CreditTVBasic
import com.rickh.movieapp.R
import org.threeten.bp.LocalDate

class KnownForAdapter(private val credits: List<CreditBasic>) :
    RecyclerView.Adapter<KnownForAdapter.KnownForViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnownForViewHolder {
        return KnownForViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        )
    }

    override fun getItemCount() = credits.size

    override fun onBindViewHolder(holder: KnownForViewHolder, position: Int) {
        holder.bind(credits[position])
    }

    class KnownForViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val creditImage: ImageView = itemView.findViewById(R.id.credit_image)
        private val title: TextView = itemView.findViewById(R.id.title_textview)
        private val byline: TextView = itemView.findViewById(R.id.byline_textview)

        fun bind(credit: CreditBasic) {
            when (credit) {
                is CreditMovieBasic -> {
                    var creditTitle = credit.title
                    if (credit.releaseDate != null && credit.releaseDate.isNotEmpty()) {
                        val date = LocalDate.parse(credit.releaseDate)
                        creditTitle += " • ${date.year}"
                    }
                    title.text = creditTitle
                    byline.text = "As ${credit.character}"
                }
                is CreditTVBasic -> {
//                    var creditTitle = credit.name
//                    if (credit.releaseDate != "") {
//                        val date = LocalDate.parse(credit.releaseDate)
//                        creditTitle += " • ${date.year}"
//                    }
//                    title.text = creditTitle
                }
            }

            if (credit.artworkPath == null)
                return

            Glide.with(creditImage)
                .load("https://image.tmdb.org/t/p/original${credit.artworkPath}")
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
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(creditImage)
        }
    }
}