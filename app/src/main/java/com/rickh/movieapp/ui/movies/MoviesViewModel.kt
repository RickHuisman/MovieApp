package com.rickh.movieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.TvShowsRepository
import com.rickh.movieapp.ui.GridItem
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.*


class MoviesViewModel : ViewModel() {

    private val _items = MutableLiveData<List<GridItem>>()
    val items: LiveData<List<GridItem>>
        get() = _items

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    private var sortMode = SortOptions.MOVIES_TOP_RATED
    private var pageIndex: Int = 1
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    fun setSortMode(sortMode: SortOptions) {
        clearList()
        this.sortMode = sortMode
        loadMore()
    }

    private fun clearList() {
        _items.value = emptyList()
        pageIndex = 1
    }

    fun loadMore() {
        coroutineScope.launch {
            _loadingProgress.postValue(true)

            var results = when (sortMode) {
                SortOptions.MOVIES_POPULAR -> MoviesRepository.getPopular(pageIndex)
                SortOptions.MOVIES_TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
                SortOptions.MOVIES_UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
                SortOptions.MOVIES_NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)

                SortOptions.TV_SHOWS_POPULAR -> TvShowsRepository.getPopular(pageIndex)
                SortOptions.TV_SHOWS_TOP_RATED -> TvShowsRepository.getTopRated(pageIndex)
                SortOptions.TV_SHOWS_ON_TV -> TvShowsRepository.getOnTheAir(pageIndex)
                SortOptions.TV_SHOWS_AIRING_TODAY -> TvShowsRepository.getAiringToday(pageIndex)
            }

            results = convertToGridItemList(results)

            withContext(Dispatchers.Main) {
                val oldItems = items.value.orEmpty()
                _items.value = getItemsForDisplay(oldItems, results)

                pageIndex++
                _loadingProgress.postValue(false)
            }
        }
    }

    private fun convertToGridItemList(items: List<Any>): List<GridItem> {
        var list = listOf<GridItem>()
        val item = items[0]
        if (item is MovieDb) {
            items as List<MovieDb>

            list = items.map { GridItem(it.id.toLong(), it.posterPath) }
        } else if (item is TvSeries) {
            items as List<TvSeries>

            list = items.map { GridItem(it.id.toLong(), it.posterPath) }
        }
        return list
    }

    private fun getItemsForDisplay(
        oldItems: List<GridItem>,
        newItems: List<GridItem>
    ): List<GridItem> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}