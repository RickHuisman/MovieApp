package com.rickh.movieapp.ui.posters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.omertron.themoviedbapi.model.media.MediaBasic
import com.rickh.movieapp.R
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.ui.posters.Category.*
import com.rickh.movieapp.ui.people.Paginator
import com.rickh.movieapp.ui.widgets.ErrorStateView
import kotlinx.android.synthetic.main.fragment_poster_grid.*

/**
 * Fragment displaying movies and tv shows posters in a grid.
 */
class PosterFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var category: Category
    private lateinit var paginator: Paginator<*>
    private lateinit var errorStateView: ErrorStateView // TODO

    private val columns = 3
    private lateinit var posterAdapter: PosterAdapter

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
            viewModel = ViewModelProvider(it).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        posterAdapter = PosterAdapter(
            columns,
            requireActivity()
        )

        category = arguments!!.getSerializable(KEY_CATEGORY) as Category

        paginator = when (category) {
            MOVIES -> viewModel.moviesPaginator
            TV_SHOWS -> viewModel.tvShowsPaginator
            DISCOVER -> TODO()
            POPULAR_PEOPLE -> TODO()
        }

        initViewModelObservers()
        setupGrid()

        error.setOnRetryClickListener(View.OnClickListener {
            paginator.loadMore()
        })
    }

    private fun initViewModelObservers() {
        paginator.items.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Result.Success -> {
                    posterAdapter.items = paginator.toPosterItems((it.data) as List<MediaBasic>)
                    checkEmptyState()
                }
                is Result.Error -> showErrorState()
            }
        })

        paginator.loadingProgress.observe(viewLifecycleOwner, Observer {
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
            ViewPreloadSizeProvider(),
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
        error.visibility = View.GONE
    }

    private fun showErrorState() {
        loading.visibility = View.GONE
        error.visibility = View.VISIBLE
    }

    companion object {
        private const val KEY_CATEGORY = "category"

        fun create(category: Category): PosterFragment {
            return PosterFragment().apply {
                val bundle = Bundle(1)
                bundle.putSerializable(KEY_CATEGORY, category)
                arguments = bundle
            }
        }
    }
}