package com.rickh.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import androidx.appcompat.widget.PopupMenu
import com.rickh.movieapp.util.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProviders
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.movies.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var sortOptionsMenu: PopupMenu
    private lateinit var categoryPagerAdapter: CategoryPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)

        setSupportActionBar(toolbar)
        setupSpinner()
        setupSortMenu()

        categoryPagerAdapter = CategoryPagerAdapter(supportFragmentManager)
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
                    val currentFragment = categoryPagerAdapter.instantiateItem(
                        fragment_container,
                        position
                    ) as CategoryFragment
                    categoryPagerAdapter.setPrimaryItem(fragment_container, position, currentFragment)

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, currentFragment)
                        .commit()

                    when (Category.ALL[position]) {
                        Category.MOVIES -> {
                            createMoviesSortOptions()
                            sort.showSort()
                        }
                        Category.TV_SHOWS -> {
                            createTvShowsSortOptions()
                            sort.showSort()
                        }
                        Category.DISCOVER -> sort.showFilter()
                        Category.POPULAR_PEOPLE -> sort.disappear()
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
        sortOptionsMenu.setOnMenuItemClickListener {
            setActiveSorting(it)
            true
        }
    }

    private fun setActiveSorting(selMenuItem: MenuItem) {
        // Reset highlighted items
        sortOptionsMenu.menu.setGroupEnabled(0, true)

        selMenuItem.isEnabled = false

        when (selMenuItem.itemId) {
            R.id.action_movies_sorting_popular -> viewModel.setSortMode(SortOptions.MOVIES_POPULAR)
            R.id.action_movies_sorting_top_rated -> viewModel.setSortMode(SortOptions.MOVIES_TOP_RATED)
            R.id.action_movies_sorting_upcoming -> viewModel.setSortMode(SortOptions.MOVIES_UPCOMING)
            R.id.action_movies_sorting_now_playing -> viewModel.setSortMode(SortOptions.MOVIES_NOW_PLAYING)

            R.id.action_tv_shows_sorting_popular -> viewModel.setSortMode(SortOptions.TV_SHOWS_POPULAR)
            R.id.action_tv_shows_sorting_top_rated -> viewModel.setSortMode(SortOptions.TV_SHOWS_TOP_RATED)
            R.id.action_tv_shows_sorting_on_tv -> viewModel.setSortMode(SortOptions.TV_SHOWS_ON_TV)
            R.id.action_tv_shows_sorting_airing_today -> viewModel.setSortMode(SortOptions.TV_SHOWS_AIRING_TODAY)
        }
    }
}
