package com.rickh.movieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickh.movieapp.tmdb.TmdbRepository
import com.rickh.movieapp.tmdb.models.Movie
import kotlinx.coroutines.*


class MoviesViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    private var pageIndex: Int = 1
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {
        loadMoreMovies()
    }

    fun loadMoreMovies() {
        coroutineScope.launch {
            _loadingProgress.postValue(true)

            val getMoviesDeferred = TmdbRepository.retrofitService.getTopRatedMovies(pageIndex)
            withContext(Dispatchers.Main) {
                val oldMovies = _movies.value.orEmpty()
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
}