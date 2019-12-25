package com.rickh.movieapp.ui.persondetail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.SingleRequest
import com.bumptech.glide.request.target.Target
import com.omertron.themoviedbapi.enumeration.CreditType
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.credits.CreditMovieBasic
import com.omertron.themoviedbapi.model.credits.CreditTVBasic
import com.rickh.movieapp.R
import org.threeten.bp.LocalDate

class KnownForViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val creditImage: ImageView = itemView.findViewById(R.id.credit_image)
    private val title: TextView = itemView.findViewById(R.id.title_textview)
    private val byline: TextView = itemView.findViewById(R.id.byline_textview)

    fun bind(credit: CreditBasic) {
        title.text = when (credit) {
            is CreditMovieBasic -> getTitle(credit.title, credit.releaseDate)
            is CreditTVBasic -> getTitle(credit.name, credit.firstAirDate)
            else -> "-"
        }
        byline.text = when (credit.creditType) {
            CreditType.CAST -> getCastByline(credit.character)
            CreditType.CREW -> getCrewByline(credit.job)
            null -> "-"
        }

        if (credit.artworkPath != null)
            bindArtwork(credit.artworkPath)
    }

    private fun getTitle(title: String, releaseDate: String?): Spannable {
        val year = getYearFromReleaseDate(releaseDate)
        return if (year.isNotEmpty()) {
            getSpannableTitleWithYear(title, year)
        } else title.toSpannable()
    }

    private fun getSpannableTitleWithYear(title: String, year: String): SpannableString {
        val text = SpannableString("$title • $year")
        val yearColor = ContextCompat.getColor(creditImage.context, R.color.gray_600)
        text.setSpan(
            ForegroundColorSpan(yearColor),
            title.length,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    private fun getYearFromReleaseDate(releaseDate: String?): String {
        return if (releaseDate.isNullOrEmpty()) "" else getYearFromDate(releaseDate)
    }

    private fun getYearFromDate(date: String): String {
        return LocalDate.parse(date).year.toString()
    }

    private fun getCastByline(character: String): String {
        return if (character.isEmpty()) "-" else "As $character"
    }

    private fun getCrewByline(job: String): String {
        return if (job.isEmpty()) "-" else job
    }

    private fun bindArtwork(artworkPath: String) {
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
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .centerCrop()
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(creditImage)
    }
}