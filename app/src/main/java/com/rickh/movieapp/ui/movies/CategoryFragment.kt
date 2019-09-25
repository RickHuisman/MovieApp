package com.rickh.movieapp.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.GridItem
import info.movito.themoviedbapi.model.MovieDb


class CategoryFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesGridAdapter
    private lateinit var category: Category
    private val columns: Int = 3

    private lateinit var loadingBar: ProgressBar
    private lateinit var grid: RecyclerView

    fun create(category: Category): CategoryFragment {
        val fragment = CategoryFragment()
        val bundle = Bundle(1)
        bundle.putSerializable(KEY_CATEGORY, category)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movies_fragment, container, false)

        loadingBar = view.findViewById(R.id.loading)
        grid = view.findViewById(R.id.grid)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = arguments!!.getSerializable(KEY_CATEGORY) as Category

        viewModel = ViewModelProviders.of(activity!!).get(MoviesViewModel::class.java)
        moviesAdapter = MoviesGridAdapter(columns, activity!!)

        initViewModelObservers()
        setupGrid()

        when(category) {
            Category.MOVIES -> viewModel.setSortMode(SortOptions.MOVIES_TOP_RATED)
            Category.TV_SHOWS -> viewModel.setSortMode(SortOptions.TV_SHOWS_TOP_RATED)
        }
    }

    private fun initViewModelObservers() {
        viewModel.items.observe(this, Observer {
            moviesAdapter.items = it
            checkEmptyState()
        })

        viewModel.loadingProgress.observe(this, Observer {
            if (it) {
                moviesAdapter.dataStartedLoading()
            } else {
                moviesAdapter.dataFinishedLoading()
            }
        })
    }

    private fun setupGrid() {
        val gridLayoutManager = GridLayoutManager(activity, columns).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return moviesAdapter.getItemColumnSpan(position)
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

        val moviePreloader = RecyclerViewPreloader(
            this,
            moviesAdapter,
            ViewPreloadSizeProvider<GridItem>(),
            6
        )

        with(grid) {
            layoutManager = gridLayoutManager
            adapter = moviesAdapter
            addOnScrollListener(infiniteScrollListener)
            addOnScrollListener(moviePreloader)
        }
    }

    private fun checkEmptyState() {
        val empty = moviesAdapter.items.isEmpty()
        loadingBar.visibility = if (empty) View.VISIBLE else View.GONE
    }

    companion object {
        private const val KEY_CATEGORY = "category"
    }
}