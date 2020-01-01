package com.rickh.movieapp.ui.persondetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omertron.themoviedbapi.enumeration.CreditType
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.rickh.movieapp.R

class KnownForAdapter(credits: List<CreditBasic>) :
    RecyclerView.Adapter<KnownForViewHolder>() {

    var displayCredits = credits.toMutableList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnownForViewHolder {
        return KnownForViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        )
    }

    override fun getItemCount() = displayCredits.size

    fun filter(activeFilters: MutableSet<String>) {
        val itemsToBeDeleted = getItemsToBeDeleted(activeFilters)
        deleteItems(itemsToBeDeleted)
    }

    private fun getItemsToBeDeleted(activeFilters: MutableSet<String>): List<CreditBasic> {
        val itemsToBeDeleted = mutableListOf<CreditBasic>()

        // Remove all acting credits
        if (!activeFilters.contains("Acting")) {
            displayCredits.forEach {
                if (it.creditType == CreditType.CAST)
                    itemsToBeDeleted.add(it)
            }
        }

        // Remove all crew credits
        displayCredits.forEach {
            if (it.creditType == CreditType.CREW) {
                if (it.department !in activeFilters) {
                    itemsToBeDeleted.add(it)
                }
            }
        }
        return itemsToBeDeleted
    }

    private fun deleteItems(itemsToBeDeleted: List<CreditBasic>) {
        itemsToBeDeleted.forEach {
            val position = displayCredits.indexOf(it)
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        displayCredits.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun addList(itemsToBeAdded: List<CreditBasic>) {
        itemsToBeAdded.forEach { addItem(it) } // TODO add list instead of each item individually
    }

    private fun addItem(item: CreditBasic) {
        displayCredits.add(item)
        notifyDataSetChanged() // TODO position
    }

    override fun onBindViewHolder(holder: KnownForViewHolder, position: Int) {
        holder.bind(displayCredits[position])
    }
}