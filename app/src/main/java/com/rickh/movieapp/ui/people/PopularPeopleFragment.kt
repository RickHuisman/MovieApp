package com.rickh.movieapp.ui.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.movies.HomeViewModel
import com.rickh.movieapp.ui.movies.InfiniteScrollListener
import kotlinx.android.synthetic.main.fragment_popular_people.*
import kotlinx.android.synthetic.main.fragment_poster_grid.*
import kotlinx.android.synthetic.main.fragment_poster_grid.loading
import timber.log.Timber

class PopularPeopleFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var peopleAdapter: PopularPeopleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        peopleAdapter = PopularPeopleAdapter(context!!)

        initViewModelObservers()
        setupList()
    }

    private fun initViewModelObservers() {
        viewModel.getPopularPeople()
        viewModel.people.observe(this, Observer {
            peopleAdapter.people = it
            checkEmptyState()
        })

        viewModel.peopleLoadingProgress.observe(this, Observer {
            if (it) {
                peopleAdapter.dataStartedLoading()
            } else {
                peopleAdapter.dataFinishedLoading()
            }
        })
    }

    private fun setupList() {
        val linearLayoutManager = LinearLayoutManager(context)
        val infiniteScrollListener = object : InfiniteScrollListener(linearLayoutManager) {
            override fun onLoadMore() {
                viewModel.getPopularPeople()
            }

            override fun isDataLoading(): Boolean {
                return viewModel.peopleLoadingProgress.value ?: false
            }
        }
        with(people_list) {
            layoutManager = linearLayoutManager
            adapter = peopleAdapter
            addOnScrollListener(infiniteScrollListener)
        }
    }

    private fun checkEmptyState() {
        val empty = peopleAdapter.people.isEmpty()
        loading.visibility = if (empty) View.VISIBLE else View.GONE
    }
}