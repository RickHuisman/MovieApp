package com.rickh.movieapp.ui.movies

import androidx.lifecycle.*
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.PeopleRepository
import com.rickh.movieapp.tmdb.TvShowsRepository
import com.rickh.movieapp.ui.PosterItem
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.people.PersonPeople
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val _items = MutableLiveData<List<PosterItem>>()
    val items: LiveData<List<PosterItem>>
        get() = _items

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    private var sortMode = SortOptions.MOVIES_TOP_RATED
    private var pageIndex: Int = 1

    private val _people = MutableLiveData<List<PersonPeople>>()
    val people: LiveData<List<PersonPeople>>
        get() = _people

    private val _peopleLoadingProgress = MutableLiveData<Boolean>()
    val peopleLoadingProgress: LiveData<Boolean>
        get() = _peopleLoadingProgress

    private var peoplePageIndex: Int = 1

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
        return if (item is MovieDb) {
            items as List<MovieDb>
            items.map { PosterItem(it.id.toLong(), it.posterPath) }
        } else {
            items as List<TvSeries>
            items.map { PosterItem(it.id.toLong(), it.posterPath) }
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

    fun getPopularPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            _peopleLoadingProgress.postValue(true)

            val oldItems = people.value.orEmpty()
            val result = PeopleRepository.getPopular(peoplePageIndex)

            _people.postValue(getItemsForDisplayPopularPeople(oldItems, result))

            peoplePageIndex++
            _peopleLoadingProgress.postValue(false)
        }
    }

    private fun getItemsForDisplayPopularPeople(
        oldItems: List<PersonPeople>,
        newItems: List<PersonPeople>
    ): List<PersonPeople> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }
}