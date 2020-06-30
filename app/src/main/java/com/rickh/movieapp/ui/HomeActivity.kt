package com.rickh.movieapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rickh.movieapp.R
import com.rickh.movieapp.data.login.LoginRepository
import com.rickh.movieapp.ui.login.LoginActivity
import com.rickh.movieapp.ui.posters.*
import com.rickh.movieapp.ui.profile.UserProfileSheetView
import com.rickh.movieapp.ui.widgets.CategoriesSpinnerAdapter
import com.rickh.movieapp.ui.widgets.ToolbarExpandableSheet
import com.rickh.movieapp.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_home.toolbar
import kotlinx.android.synthetic.main.activity_home.toolbar_sheet

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
        categoryPagerAdapter =
            CategoryPagerAdapter(
                supportFragmentManager
            )

        setSupportActionBar(toolbar)
        setupSpinner()
        setupSortOptions()
        setUpProfile()
        setupToolbarSheet()

        val loginRepository = LoginRepository.getInstance(this)
        loginRepository.userLoggedInObserver.observe(this, Observer {
            if (loginRepository.isLoggedIn) handleOnUserLogIn()
        })
    }

    private fun handleOnUserLogIn() {
        if (toolbar_sheet.isCollapsed) showUserProfileSheet()
    }

    private fun setupToolbarSheet() {
        toolbar_sheet.hideOnOutsideClick(fragment_container)
        toolbar_sheet.setStateChangeListener { state ->
            when (state) {
                ToolbarExpandableSheet.State.COLLAPSED -> {
                    toolbar_sheet.removeAllViews()
                    toolbar_sheet.collapse()
                }
            }
        }
    }

    private fun showUserProfileSheet() {
        val sheet = UserProfileSheetView(this).showIn(toolbar_sheet)
        sheet.post { toolbar_sheet.expand() }
    }

    private fun setUpProfile() {
        profile.setOnClickListener {
            if (LoginRepository.getInstance(this).isLoggedIn) {
                showUserProfileSheet()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun setupSpinner() {
        with(spinner) {
            adapter = CategoriesSpinnerAdapter(context)
            dropDownVerticalOffset = ViewUtils.dpToPix(context, 8)
            onItemSelectedListener = spinnerOnItemSelected
        }
    }

    private val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>) {

        }

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            setCurrentFragment(position)

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
        ) as Fragment
        categoryPagerAdapter.setPrimaryItem(fragmentContainer, position, currentFragment)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, currentFragment)
            .commit()
    }

    private fun setupSortOptions() {
        sortOptionsMenu = PopupMenu(
            this, sort_filter_button, Gravity.END, 0, R.style.MovieAppPopupMenu_MoviesSort
        )
        with(sortOptionsMenu) {
            menuInflater.inflate(R.menu.menu_movies_sorting_mode, sortOptionsMenu.menu)
            setOnMenuItemClickListener {
                setActiveSortOption(it.itemId)
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

        // Default sort option
        val defaultSortOption =
            if (menuResId == R.menu.menu_movies_sorting_mode)
                R.id.action_movies_sorting_top_rated
            else
                R.id.action_tv_shows_sorting_top_rated
        setActiveSortOption(defaultSortOption)
    }

    private fun setActiveSortOption(menuItemId: Int) {
        // Reset highlighted items
        sortOptionsMenu.menu.setGroupEnabled(0, true)

        val selectedMenuItem = sortOptionsMenu.menu.findItem(menuItemId)
        selectedMenuItem.isEnabled = false

        when (menuItemId) {
            R.id.action_movies_sorting_popular -> viewModel.moviesPaginator.setSortMode(
                MoviesSortOptions.POPULAR
            )
            R.id.action_movies_sorting_top_rated -> viewModel.moviesPaginator.setSortMode(
                MoviesSortOptions.TOP_RATED
            )
            R.id.action_movies_sorting_upcoming -> viewModel.moviesPaginator.setSortMode(
                MoviesSortOptions.UPCOMING
            )
            R.id.action_movies_sorting_now_playing -> viewModel.moviesPaginator.setSortMode(
                MoviesSortOptions.NOW_PLAYING
            )

            R.id.action_tv_shows_sorting_popular -> viewModel.tvShowsPaginator.setSortMode(
                TVShowsSortOptions.POPULAR
            )
            R.id.action_tv_shows_sorting_top_rated -> viewModel.tvShowsPaginator.setSortMode(
                TVShowsSortOptions.TOP_RATED
            )
            R.id.action_tv_shows_sorting_on_tv -> viewModel.tvShowsPaginator.setSortMode(
                TVShowsSortOptions.ON_TV
            )
            R.id.action_tv_shows_sorting_airing_today -> viewModel.tvShowsPaginator.setSortMode(
                TVShowsSortOptions.AIRING_TODAY
            )
            else -> throw IllegalArgumentException("No sort option for menuItemId: $menuItemId")
        }
    }

    override fun onBackPressed() {
        if (!toolbar_sheet.isCollapsed) {
            toolbar_sheet.collapse()
        } else {
            super.onBackPressed()
        }
    }
}
