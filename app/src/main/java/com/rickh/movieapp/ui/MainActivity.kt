package com.rickh.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.widget.ArrayAdapter
import androidx.appcompat.widget.PopupMenu
import com.rickh.movieapp.ui.movies.MoviesFragment
import com.rickh.movieapp.util.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.core.view.get
import com.rickh.movieapp.R


class MainActivity : AppCompatActivity() {

    private lateinit var sortOptionsMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupSpinner()
        setupSortMenu()

        // Start fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MoviesFragment(this)).commit()
    }

    private fun setupSpinner() {
        val categories = resources.getStringArray(R.array.movie_app_categories)
        val categoriesAdapter = ArrayAdapter(
            this,
            R.layout.spinner_category_selected_item,
            android.R.id.text1,
            categories
        )
        categoriesAdapter.setDropDownViewResource(R.layout.item_spinner_category)

        with(spinner) {
            adapter = categoriesAdapter
            dropDownVerticalOffset = ViewUtils.dpToPix(context, 8)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            createMoviesSortOptions()
                            sort.showSort()
                        }
                        1 -> {
                            createTvShowsSortOptions()
                            sort.showSort()
                        }
                        2 -> sort.showFilter()
                        3 -> sort.disappear()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
    }

    private fun createMoviesSortOptions() {
        sortOptionsMenu.menu.clear()
        sortOptionsMenu.menuInflater.inflate(R.menu.menu_movies_sorting_mode, sortOptionsMenu.menu)
    }

    private fun createTvShowsSortOptions() {
        sortOptionsMenu.menu.clear()
        sortOptionsMenu.menuInflater.inflate(
            R.menu.menu_tv_shows_sorting_mode,
            sortOptionsMenu.menu
        )
    }

    private fun setupSortMenu() {
        createSortMenu(sort)

        sort.setOnClickListener {
            sortOptionsMenu.show()
        }
    }

    private fun createSortMenu(v: View) {
        sortOptionsMenu = PopupMenu(
            this, v, Gravity.END, 0, R.style.MovieAppPopupMenu_MoviesSort
        )

        sortOptionsMenu.menuInflater.inflate(R.menu.menu_movies_sorting_mode, sortOptionsMenu.menu)
        sortOptionsMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_movies_sorting_popular -> Timber.d("Popular")
                R.id.action_movies_sorting_top_rated -> Timber.d("Top rated")
                R.id.action_movies_sorting_upcoming -> Timber.d("Upcoming")
                R.id.action_movies_sorting_now_playing -> Timber.d("Now playing")
            }
            highlightActiveSorting(menuItem)
            true
        }
    }

    private fun highlightActiveSorting(selMenuItem: MenuItem) {
        val menu = sortOptionsMenu.menu

        // Reset highlighted items
        menu.setGroupEnabled(0, true)

        for (i in 0 until menu.size()) {
            if (selMenuItem.title == menu[i].title) {
                menu[i].isEnabled = false
            }
        }
    }
}
