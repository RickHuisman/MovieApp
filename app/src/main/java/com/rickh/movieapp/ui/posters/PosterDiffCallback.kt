package com.rickh.movieapp.ui.posters

import androidx.recyclerview.widget.DiffUtil

class PosterDiffCallback(
    private var oldItemsList: List<PosterItem>,
    private var newItemsList: List<PosterItem>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemsList[oldItemPosition].id == newItemsList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldItemsList.size
    }

    override fun getNewListSize(): Int {
        return newItemsList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemsList[oldItemPosition] == newItemsList[newItemPosition]
    }
}
