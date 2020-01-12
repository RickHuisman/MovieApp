package com.rickh.movieapp.ui.persondetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omertron.themoviedbapi.enumeration.CreditType
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.rickh.movieapp.R

class PersonCreditsAdapter :
    RecyclerView.Adapter<PersonCreditViewHolder>() {

    var credits: List<CreditBasic> = emptyList()
        set(value) {
            field = value
            displayCredits = value.toMutableList()
            notifyDataSetChanged()
        }
    private var displayCredits = credits.toMutableList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonCreditViewHolder {
        return PersonCreditViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        )
    }

    override fun getItemCount() = displayCredits.size

    fun filter(filter: String, active: Boolean) {
        if (active) {
            val itemsToAdd = getItemsForFilter(filter, credits)
            addItems(itemsToAdd)
        } else {
            val itemsToRemove = getItemsForFilter(filter, displayCredits)
            deleteItems(itemsToRemove)
        }
    }

    private fun getItemsForFilter(filter: String, list: List<CreditBasic>): MutableList<CreditBasic> {
        val items = mutableListOf<CreditBasic>()

        if (filter == "Acting") {
            list.forEach {
                if (it.creditType == CreditType.CAST)
                    items.add(it)
            }
        } else {
            list.forEach {
                if (it.department == filter)
                    items.add(it)
            }
        }

        return items
    }

    private fun deleteItems(itemsToBeDeleted: List<CreditBasic>) {
        itemsToBeDeleted.forEach {
            val positionOfItem = displayCredits.indexOf(it)
            displayCredits.removeAt(positionOfItem)
            notifyItemRemoved(positionOfItem)
        }
    }

    private fun addItems(itemsToBeAdded: List<CreditBasic>) {
        displayCredits.addAll(itemsToBeAdded)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PersonCreditViewHolder, position: Int) {
        holder.bind(displayCredits[position])
    }
}