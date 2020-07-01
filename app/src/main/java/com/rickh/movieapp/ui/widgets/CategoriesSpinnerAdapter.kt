package com.rickh.movieapp.ui.widgets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rickh.movieapp.R
import android.view.LayoutInflater
import com.rickh.movieapp.ui.posters.Category
import kotlinx.android.synthetic.main.item_spinner_category.view.*

class CategoriesSpinnerAdapter(context: Context) :
    ArrayAdapter<String>(context, R.layout.item_spinner_category) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Selected item layout
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_category_selected_item,
            parent,
            false
        )
        view.title.setText(Category.ALL[position].titleRes())
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_spinner_category,
            parent,
            false
        )
        with(Category.ALL[position]) {
            view.icon.setImageResource(this.iconRes())
            view.title.setText(this.titleRes())
        }
        return view
    }

    override fun getCount(): Int {
        return Category.ALL.size
    }
}