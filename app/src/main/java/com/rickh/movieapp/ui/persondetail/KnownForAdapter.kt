package com.rickh.movieapp.ui.persondetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.rickh.movieapp.R

class KnownForAdapter(private val credits: List<CreditBasic>) :
    RecyclerView.Adapter<KnownForViewHolder>() {

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
}