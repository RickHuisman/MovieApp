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
import com.omertron.themoviedbapi.model.media.MediaBasic
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.people.Paginator
import kotlinx.android.synthetic.main.fragment_poster_grid.*

/**
 * Fragment displaying movies and tvshows posters in a grid.
 */
class PosterFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var category: Category
    private lateinit var paginator: Paginator<*>

    private val columns: Int = 3
    private lateinit var posterAdapter: PosterAdapter

    fun create(category: Category): PosterFragment {
        return PosterFragment().apply {
            val bundle = Bundle(2)
            bundle.putSerializable(KEY_CATEGORY, category)
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poster_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        posterAdapter = PosterAdapter(columns, activity!!)

        category = arguments!!.getSerializable(KEY_CATEGORY) as Category

        if (category == Category.MOVIES) {
            paginator = viewModel.moviesPaginator
        } else if (category == Category.TV_SHOWS) {
            paginator = viewModel.tvShowsPaginator
        }

        initViewModelObservers()
        setupGrid()
    }

    private fun initViewModelObservers() {
        paginator.loadMore()
        paginator.items.observe(this, Observer {
            posterAdapter.items = paginator.convertToPosterItems(it as List<MediaBasic>)
            checkEmptyState()
        })

        paginator.loadingProgress.observe(this, Observer {
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
                paginator.loadMore()
            }

            override fun isDataLoading(): Boolean {
                return paginator.loadingProgress.value ?: false
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