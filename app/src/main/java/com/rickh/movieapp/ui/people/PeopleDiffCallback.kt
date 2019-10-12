package com.rickh.movieapp.ui.people

import androidx.recyclerview.widget.DiffUtil
import com.omertron.themoviedbapi.model.person.PersonFind

class PeopleDiffCallback(
    private val oldList: List<PersonFind>,
    private val newList: List<PersonFind>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}