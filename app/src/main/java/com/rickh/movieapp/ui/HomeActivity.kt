package com.rickh.movieapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.zagum.expandicon.ExpandIconView
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.discover.DiscoverFilterSheetView
import com.rickh.movieapp.ui.discover.ToolbarExpandableSheet
import com.rickh.movieapp.ui.login.LoginActivity
import com.rickh.movieapp.ui.movies.*
import com.rickh.movieapp.ui.widgets.CategoriesSpinnerAdapter
import com.rickh.movieapp.utils.ViewUtils
import timber.log.Timber

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
        setupSortOptions()
        setUpProfile()
        setupToolbarSheet()

        window.decorView.apply {
            // Transparent navigation bar
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            // Add inset because fitsSystemWindows doesn't work when you hide the navigation bar
            setOnApplyWindowInsetsListener { _, insets ->
                handleInsets(insets)
                insets.consumeSystemWindowInsets()
            }
        }
    }

    private fun handleInsets(insets: WindowInsets) {
        // inset the toolbar down by the status bar height
        val lpToolbar = (toolbar.layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin += insets.systemWindowInsetTop
            leftMargin += insets.systemWindowInsetLeft
            rightMargin += insets.systemWindowInsetRight
        }
        toolbar_title_container.layoutParams = lpToolbar

        // we place a background behind the status bar to combine with it's semi-transparent
        // color to get the desired appearance.  Set it's height to the status bar height
        val statusBarBackground = findViewById<View>(R.id.status_bar_background)
        val lpStatus = (statusBarBackground.layoutParams as RelativeLayout.LayoutParams).apply {
            height = insets.systemWindowInsetTop
        }
        statusBarBackground.layoutParams = lpStatus

        // clear this listener so insets aren't re-applied
        window.decorView.setOnApplyWindowInsetsListener(null)
    }

    private fun setUpProfile() {
        profile.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupToolbarSheet() {
        toolbar_sheet.hideOnOutsideClick(fragment_container)
        toolbar_sheet.setStateChangeListener { state ->
            when (state) {
                ToolbarExpandableSheet.State.EXPANDING -> {
//                    if (isSubredditPickerVisible()) {
//                        // When subreddit picker is showing, we'll show a "configure subreddits" button in the toolbar.
//                        invalidateOptionsMenu()
//
//                    } else if (isUserProfileSheetVisible()) {
//                        title = getString(
//                            R.string.user_name_u_prefix,
//                            userSessionRepository.get().loggedInUserName()
//                        )
//                    }

//                    toolbarTitleArrowView.setState(ExpandIconView.LESS, true)
                }

                ToolbarExpandableSheet.State.EXPANDED -> {
                }

                ToolbarExpandableSheet.State.COLLAPSING -> {
//                    if (isSubredditPickerVisible()) {
//                        Keyboards.hide(this, toolbarSheet)
//
//                    } else if (isUserProfileSheetVisible()) {
//                        // This will update the title.
//                        setTitle(subredditChangesStream.getValue())
//                    }
//                    toolbarTitleArrowView.setState(ExpandIconView.MORE, true)
                }

                ToolbarExpandableSheet.State.COLLAPSED -> {
                    toolbar_sheet.removeAllViews()
                    toolbar_sheet.collapse()
                }
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
                Category.DISCOVER -> setDiscoverFilter()
                Category.POPULAR_PEOPLE -> sort_filter_button.disappear()
            }
        }
    }

    private fun setDiscoverFilter() {
        sort_filter_button.showFilter()
        sort_filter_button.setOnClickListener {
            showDiscoverFilterSheet()
        }
    }

    private fun showDiscoverFilterSheet() {
        val filterSheet = DiscoverFilterSheetView.showIn(toolbar_sheet)
        filterSheet.post { toolbar_sheet.expand() }
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
}
