package com.rickh.movieapp.ui.persondetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rickh.movieapp.R

class FiltersAdapter(
    private val filters: List<String>,
    private val onFilterChanged: OnFilterChanged
) :
    RecyclerView.Adapter<FiltersAdapter.FilterViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val filterButton: Button = itemView.findViewById(R.id.item_filter)
        private var filterActive: Boolean = true

        fun bind(filter: String) {
            filterButton.text = filter

            itemView.setOnClickListener {
                filterActive = !filterActive
                setFilterMode(filterActive)
                onFilterChanged.filterChanged(filter, filterActive)
            }
        }

        private fun setFilterMode(active: Boolean) {
            val normalTextColor = ContextCompat.getColor(filterButton.context, R.color.gray_400)
            val highlightedTextColor =
                ContextCompat.getColor(filterButton.context, R.color.gray_700)

            if (active) {
                filterButton.setTextColor(normalTextColor)
                filterButton.background.alpha = 255
            } else {
                filterButton.setTextColor(highlightedTextColor)
                filterButton.background.alpha = 100
            }
        }
    }
}