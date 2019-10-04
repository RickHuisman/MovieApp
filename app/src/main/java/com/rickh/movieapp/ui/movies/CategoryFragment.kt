package com.rickh.movieapp.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.PosterItem
import kotlinx.android.synthetic.main.poster_grid_fragment.*
import java.lang.IllegalArgumentException

/**
 * Fragment displaying movies and tvshows posters in a grid.
 */
class CategoryFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var category: Category

    private val columns: Int = 3
    private lateinit var posterAdapter: PosterAdapter

    fun create(category: Category): CategoryFragment {
        return CategoryFragment().apply {
            val bundle = Bundle(1)
            bundle.putSerializable(KEY_CATEGORY, category)
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.poster_grid_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
        }

        posterAdapter = PosterAdapter(columns, activity!!)

        initViewModelObservers()
        setupGrid()

        category = arguments!!.getSerializable(KEY_CATEGORY) as Category
        val sortOption = when (category) {
            Category.MOVIES -> SortOptions.MOVIES_TOP_RATED
            Category.TV_SHOWS -> SortOptions.TV_SHOWS_TOP_RATED
            else -> throw IllegalArgumentException("No default sort option for category: $category")
        }
        viewModel.setSortMode(sortOption)
    }

    private fun initViewModelObservers() {
        viewModel.items.observe(this, Observer {
            posterAdapter.items = it
            checkEmptyState()
        })

        viewModel.loadingProgress.observe(this, Observer {
            if (it) {
                posterAdapter.dataStartedLoading()
            } else {
                posterAdapter.dataFinishedLoading()
            }
        })
    }

    private fun setupGrid() {
        val gridLayoutManager = GridLayoutManager(activity, columns).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return posterAdapter.getItemColumnSpan(position)
                }
            }
        }
        val infiniteScrollListener = object : InfiniteScrollListener(gridLayoutManager) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }

            override fun isDataLoading(): Boolean {
                return viewModel.loadingProgress.value ?: false
            }
        }
        val posterPreloader = RecyclerViewPreloader(
            this,
            posterAdapter,
            ViewPreloadSizeProvider<PosterItem>(),
            6
        )

        with(grid) {
            layoutManager = gridLayoutManager
            adapter = posterAdapter
            addOnScrollListener(infiniteScrollListener)
            addOnScrollListener(posterPreloader)
        }
    }

    private fun checkEmptyState() {
        val empty = posterAdapter.items.isEmpty()
        loading.visibility = if (empty) View.VISIBLE else View.GONE
    }

    companion object {
        private const val KEY_CATEGORY = "category"
    }
}