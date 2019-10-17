package com.rickh.movieapp.ui

import com.omertron.themoviedbapi.enumeration.MediaType
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.tmdb.TVShowsRepository
import com.rickh.movieapp.ui.movies.PosterItem
import com.rickh.movieapp.ui.movies.TVShowsSortOptions
import com.rickh.movieapp.ui.people.Paginator
import kotlinx.coroutines.launch

class TVShowsPaginator : Paginator<TVInfo>() {

    private var sortOption = TVShowsSortOptions.TOP_RATED

    override fun loadMore() {
        scope.launch {
            loadingProgress.postValue(true)

            val oldItems = items.value.orEmpty()
            val newItems = when (sortOption) {
                TVShowsSortOptions.POPULAR -> TVShowsRepository.getPopular(pageIndex)
                TVShowsSortOptions.TOP_RATED -> TVShowsRepository.getTopRated(pageIndex)
                TVShowsSortOptions.ON_TV -> TVShowsRepository.getOnTheAir(pageIndex)
                TVShowsSortOptions.AIRING_TODAY -> TVShowsRepository.getAiringToday(pageIndex)
            }
            items.postValue(getItemsForDisplay(oldItems, newItems))

            pageIndex++
            loadingProgress.postValue(false)
        }
    }

    fun convertTv(movies: List<TVInfo>): List<PosterItem> {
        return movies.map {
            PosterItem(
                it.id.toLong(),
                it.posterPath,
                MediaType.TV
            )
        }
    }

    private fun getItemsForDisplay(
        oldItems: List<TVInfo>,
        newItems: List<TVInfo>
    ): List<TVInfo> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }

    fun setSortMode(sortOption: TVShowsSortOptions) {
        clearList()
        this.sortOption = sortOption
        loadMore()
    }

    private fun clearList() {
        items.value = emptyList()
        pageIndex = 1
    }
}