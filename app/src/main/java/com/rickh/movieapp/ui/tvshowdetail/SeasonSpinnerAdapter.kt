package com.rickh.movieapp.ui.tvshowdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.rickh.movieapp.R
import kotlinx.android.synthetic.main.item_spinner_category.view.*

// TODO Merge with CategoriesSpinnerAdapter.
class SeasonSpinnerAdapter(
    context: Context,
    private val seasons: List<TVSeasonBasic>
) : ArrayAdapter<String>(context, R.layout.item_spinner_season) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Selected item layout
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_category_selected_item,
            parent,
            false
        )
        view.title.text = seasons[position].name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_spinner_season,
            parent,
            false
        )

        view.title.text = seasons[position].name

        return view
    }

    override fun getCount(): Int {
        return seasons.size
    }
}