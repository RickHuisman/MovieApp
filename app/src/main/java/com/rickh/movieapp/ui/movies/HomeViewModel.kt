package com.rickh.movieapp.ui.movies

import androidx.lifecycle.*
import com.rickh.movieapp.data.tmdb.MoviesRepository
import com.rickh.movieapp.ui.people.PopularPeoplePaginator
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    val moviesPaginator = MoviesPaginator()
    val tvShowsPaginator = TVShowsPaginator()
    val peoplePaginator = PopularPeoplePaginator()

    fun getMovie(movieId: Long) = liveData(Dispatchers.IO) {
        emit(MoviesRepository.getMovieInfo(movieId))
    }

    override fun onCleared() {
        super.onCleared()
        moviesPaginator.cancelLoading()
        tvShowsPaginator.cancelLoading()
        peoplePaginator.cancelLoading()
    }
}