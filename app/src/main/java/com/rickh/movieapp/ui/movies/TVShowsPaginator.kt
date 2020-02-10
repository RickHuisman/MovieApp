package com.rickh.movieapp.ui.movies

import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.data.tmdb.TVShowsRepository
import com.rickh.movieapp.ui.movies.TVShowsSortOptions
import com.rickh.movieapp.ui.people.Paginator

class TVShowsPaginator : Paginator<TVInfo>() {

    private var sortOption = TVShowsSortOptions.TOP_RATED

    override fun loadData(): Result<Any> {
        return when (sortOption) {
            TVShowsSortOptions.POPULAR -> TVShowsRepository.getPopular(pageIndex)
            TVShowsSortOptions.TOP_RATED -> TVShowsRepository.getTopRated(pageIndex)
            TVShowsSortOptions.ON_TV -> TVShowsRepository.getOnTheAir(pageIndex)
            TVShowsSortOptions.AIRING_TODAY -> TVShowsRepository.getAiringToday(pageIndex)
        }
    }

    fun setSortMode(sortOption: TVShowsSortOptions) {
        clearList()
        this.sortOption = sortOption
        loadMore()
    }
}