package com.rickh.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupSpinner()
    }

    private fun setupSpinner() {
        val categories = resources.getStringArray(R.array.movie_app_categories)
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_category_selected_item,
            android.R.id.text1,
            categories
        )

        adapter.setDropDownViewResource(R.layout.item_spinner_category)
        spinner.adapter = adapter
        spinner.dropDownVerticalOffset = dpToPix(8)
    }

    private fun dpToPix(dp: Int): Int {
        return (dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}
