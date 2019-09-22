package com.rickh.movieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.models.Movie
import kotlinx.coroutines.*


class MoviesViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    private var sortMode: String = SORT_MODE_NOW_PLAYING
    private var pageIndex: Int = 1
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {
        loadMore()
    }

    fun setSortMode(sortMode: String) {
        clearList()
        this.sortMode = when (sortMode) {
            "Popular" -> SORT_MODE_POPULAR
            "Top rated" -> SORT_MODE_TOP_RATED
            "Upcoming" -> SORT_MODE_UPCOMING
            "Now playing" -> SORT_MODE_NOW_PLAYING
            else -> throw IllegalStateException("Unsupported sort mode")
        }
        loadMore()
    }

    private fun clearList() {
        _movies.value = emptyList()
        pageIndex = 1
    }

    fun loadMore() {
        coroutineScope.launch {
            _loadingProgress.postValue(true)

            val getMoviesDeferred = when (sortMode) {
                SORT_MODE_POPULAR -> MoviesRepository.getPopular(pageIndex)
                SORT_MODE_TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
                SORT_MODE_UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
                SORT_MODE_NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)
                else -> throw IllegalStateException("Unsupported sort mode")
            }

            withContext(Dispatchers.Main) {
                val oldMovies = movies.value.orEmpty()
                _movies.value = getMoviesForDisplay(oldMovies, getMoviesDeferred.results)

                pageIndex++
                _loadingProgress.postValue(false)
            }
        }
    }

    private fun getMoviesForDisplay(
        oldMovies: List<Movie>,
        newMovies: List<Movie>
    ): List<Movie> {
        val moviesToBeDisplayed = oldMovies.toMutableList()
        moviesToBeDisplayed.addAll(newMovies)
        return moviesToBeDisplayed
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        private const val SORT_MODE_POPULAR = "Popular"
        private const val SORT_MODE_TOP_RATED = "Top rated"
        private const val SORT_MODE_UPCOMING = "Upcoming"
        private const val SORT_MODE_NOW_PLAYING = "Now playing"
    }
}