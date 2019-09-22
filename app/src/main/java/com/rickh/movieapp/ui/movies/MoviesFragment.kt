package com.rickh.movieapp.ui.movies

import android.content.Context
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
import com.rickh.movieapp.tmdb.models.Movie


class MoviesFragment(
    private val test: Context
) : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var loadingBar: ProgressBar
    private lateinit var grid: RecyclerView
    private lateinit var moviesAdapter: MoviesGridAdapter
    private val columns: Int = 3

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MoviesViewModel::class.java)
        moviesAdapter = MoviesGridAdapter(columns, test)

        initViewModelObservers()
        setupGrid()
    }

    private fun initViewModelObservers() {
        viewModel.movies.observe(this, Observer {
            moviesAdapter.movies = it
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
        val gridLayoutManager = GridLayoutManager(context, columns).apply {
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
            ViewPreloadSizeProvider<Movie>(),
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
        val empty = moviesAdapter.movies.isEmpty()
        loadingBar.visibility = if (empty) View.VISIBLE else View.GONE
    }
}