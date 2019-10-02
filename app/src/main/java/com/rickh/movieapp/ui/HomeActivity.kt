package com.rickh.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import androidx.appcompat.widget.PopupMenu
import com.rickh.movieapp.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_home.*
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.movies.*

/**
 * Main activity
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryPagerAdapter: CategoryPagerAdapter
    private lateinit var sortOptionsMenu: PopupMenu
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        fragmentContainer = fragment_container
        categoryPagerAdapter = CategoryPagerAdapter(supportFragmentManager)

        setSupportActionBar(toolbar)
        setupSpinner()
        setupSortOptionsMenu()
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
            onItemSelectedListener = spinnerOnItemSelected
        }
    }

    private val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>) {

        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            setCurrentFragment(position)

            // Sort options
            when (Category.ALL[position]) {
                Category.MOVIES -> setSortOptions(R.menu.menu_movies_sorting_mode)
                Category.TV_SHOWS -> setSortOptions(R.menu.menu_tv_shows_sorting_mode)
                Category.DISCOVER -> sort_filter_button.showFilter()
                Category.POPULAR_PEOPLE -> sort_filter_button.disappear()
            }
        }
    }

    private fun setCurrentFragment(position: Int) {
        val currentFragment = categoryPagerAdapter.instantiateItem(
            fragmentContainer,
            position
        ) as CategoryFragment
        categoryPagerAdapter.setPrimaryItem(fragmentContainer, position, currentFragment)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, currentFragment)
            .commit()
    }

    private fun setupSortOptionsMenu() {
        sortOptionsMenu = PopupMenu(
            this, sort_filter_button, Gravity.END, 0, R.style.MovieAppPopupMenu_MoviesSort
        )
        with(sortOptionsMenu) {
            menuInflater.inflate(R.menu.menu_movies_sorting_mode, sortOptionsMenu.menu)
            setOnMenuItemClickListener {
                setActiveSortOption(it)
                true
            }
        }

        sort_filter_button.setOnClickListener {
            sortOptionsMenu.show()
        }
    }

    private fun setSortOptions(menuResId: Int) {
        sortOptionsMenu.menu.clear()
        sortOptionsMenu.menuInflater.inflate(menuResId, sortOptionsMenu.menu)
        sort_filter_button.showSort()
    }

    private fun setActiveSortOption(selMenuItem: MenuItem) {
        // Reset highlighted items
        sortOptionsMenu.menu.setGroupEnabled(0, true)

        selMenuItem.isEnabled = false

        val sortOption = when (selMenuItem.itemId) {
            R.id.action_movies_sorting_popular -> SortOptions.MOVIES_POPULAR
            R.id.action_movies_sorting_top_rated -> SortOptions.MOVIES_TOP_RATED
            R.id.action_movies_sorting_upcoming -> SortOptions.MOVIES_UPCOMING
            R.id.action_movies_sorting_now_playing -> SortOptions.MOVIES_NOW_PLAYING

            R.id.action_tv_shows_sorting_popular -> SortOptions.TV_SHOWS_POPULAR
            R.id.action_tv_shows_sorting_top_rated -> SortOptions.TV_SHOWS_TOP_RATED
            R.id.action_tv_shows_sorting_on_tv -> SortOptions.TV_SHOWS_ON_TV
            R.id.action_tv_shows_sorting_airing_today -> SortOptions.TV_SHOWS_AIRING_TODAY
            else -> throw IllegalArgumentException("No sort option for selected menu item")
        }
        viewModel.setSortMode(sortOption)
    }
}
