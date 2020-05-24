package com.rickh.movieapp.ui.tvshowdetail.seasons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.rickh.movieapp.R
import timber.log.Timber

class EpisodesAdapter(
    private val episodes: List<TVEpisodeInfo>
) : RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EpisodeViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
    )

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        Timber.d("${episodes[position]}")
        holder.bind(episodes[position])
    }

    class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val backdropImageView: ImageView = itemView.findViewById(R.id.backdrop)
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val overviewTextView: TextView = itemView.findViewById(R.id.overview)

        fun bind(episode: TVEpisodeInfo) {
            /*
                    Glide.with(poster)
            .load(
                poster.context.getString(R.string.tmdb_base_img_url, item.posterPath)
            )
             */

            Glide.with(backdropImageView)
                .load(
                    backdropImageView.context.getString(R.string.tmdb_base_img_url, episode.stillPath)
                )
                .into(backdropImageView)
            nameTextView.text = episode.name
            overviewTextView.text = episode.overview
        }
    }
}