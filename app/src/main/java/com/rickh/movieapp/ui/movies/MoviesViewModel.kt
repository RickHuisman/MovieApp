package com.rickh.movieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.TvShowsRepository
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.MovieDb
import kotlinx.coroutines.*


class MoviesViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDb>>()
    val movies: LiveData<List<MovieDb>>
        get() = _movies

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    private var sortMode = MoviesSortOptions.TOP_RATED
    private var pageIndex: Int = 1
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {
        loadMore()
    }

    fun setSortMode(sortMode: MoviesSortOptions) {
        clearList()
        this.sortMode = sortMode
        loadMore()
    }

    private fun clearList() {
        _movies.value = emptyList()
        pageIndex = 1
    }

    fun loadMore() {
        coroutineScope.launch {
            _loadingProgress.postValue(true)

            val results = when (sortMode) {
                MoviesSortOptions.POPULAR -> MoviesRepository.getPopular(pageIndex)
                MoviesSortOptions.TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
                MoviesSortOptions.UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
                MoviesSortOptions.NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)
            }

            withContext(Dispatchers.Main) {
                val oldMovies = movies.value.orEmpty()
                _movies.value = getMoviesForDisplay(oldMovies, results)

                pageIndex++
                _loadingProgress.postValue(false)
            }
        }
    }

    private fun getMoviesForDisplay(
        oldMovies: List<MovieDb>,
        newMovies: List<MovieDb>
    ): List<MovieDb> {
        val moviesToBeDisplayed = oldMovies.toMutableList()
        moviesToBeDisplayed.addAll(newMovies)
        return moviesToBeDisplayed
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}