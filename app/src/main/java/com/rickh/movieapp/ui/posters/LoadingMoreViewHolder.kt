package com.rickh.movieapp.ui.posters

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView

class LoadingMoreViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val progress = itemView as ProgressBar

    fun bind(position: Int, showLoadingMore: Boolean) {
        progress.visibility =
            if (position > 0 && showLoadingMore) View.VISIBLE else View.INVISIBLE
    }
}
