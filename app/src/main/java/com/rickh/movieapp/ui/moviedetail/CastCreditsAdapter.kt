package com.rickh.movieapp.ui.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omertron.themoviedbapi.model.credits.MediaCreditCast
import com.rickh.movieapp.R

class CastCreditsAdapter(
    private val credits: List<MediaCreditCast>
) : RecyclerView.Adapter<CastCreditViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastCreditViewHolder {
        return CastCreditViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        )
    }

    override fun getItemCount() = credits.size

    override fun onBindViewHolder(holder: CastCreditViewHolder, position: Int) {
        holder.bind(credits[position])
    }
}