package com.rickh.movieapp.ui.movies

import androidx.lifecycle.*
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.TvShowsRepository
import com.rickh.movieapp.ui.PosterItem
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.IllegalArgumentException


class HomeViewModel : ViewModel() {

    private val _items = MutableLiveData<List<PosterItem>>()
    val items: LiveData<List<PosterItem>>
        get() = _items

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    private var sortMode = SortOptions.MOVIES_TOP_RATED
    private var pageIndex: Int = 1

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
        viewModelScope.launch(Dispatchers.IO) {
            _loadingProgress.postValue(true)

            val results = when (sortMode) {
                SortOptions.MOVIES_POPULAR -> MoviesRepository.getPopular(pageIndex)
                SortOptions.MOVIES_TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
                SortOptions.MOVIES_UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
                SortOptions.MOVIES_NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)

                SortOptions.TV_SHOWS_POPULAR -> TvShowsRepository.getPopular(pageIndex)
                SortOptions.TV_SHOWS_TOP_RATED -> TvShowsRepository.getTopRated(pageIndex)
                SortOptions.TV_SHOWS_ON_TV -> TvShowsRepository.getOnTheAir(pageIndex)
                SortOptions.TV_SHOWS_AIRING_TODAY -> TvShowsRepository.getAiringToday(pageIndex)
            }

            val oldItems = items.value.orEmpty()
            _items.postValue(getItemsForDisplay(oldItems, convertToPosterItemList(results)))

            pageIndex++
            _loadingProgress.postValue(false)
        }
    }

    private fun convertToPosterItemList(items: List<Any>): List<PosterItem> {
        val item = items[0]
        if (item is MovieDb) {
            items as List<MovieDb>
            return items.map { PosterItem(it.id.toLong(), it.posterPath) }
        } else {
            items as List<TvSeries>
            return items.map { PosterItem(it.id.toLong(), it.posterPath) }
        }
    }

    private fun getItemsForDisplay(
        oldItems: List<PosterItem>,
        newItems: List<PosterItem>
    ): List<PosterItem> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }

    fun getMovie(movieId: Long) = liveData(Dispatchers.IO) {
        emit(MoviesRepository.getMovie(movieId))
    }
}