package com.rickh.movieapp.ui.posters

import androidx.lifecycle.*
import com.rickh.movieapp.data.tmdb.MoviesRepository
import com.rickh.movieapp.data.tmdb.TVShowsRepository
import com.rickh.movieapp.ui.TVShowsPaginator
import com.rickh.movieapp.ui.people.PopularPeoplePaginator
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    val moviesPaginator = MoviesPaginator()
    val tvShowsPaginator = TVShowsPaginator()
    val peoplePaginator = PopularPeoplePaginator()

    fun getMovie(movieId: Int) = liveData(Dispatchers.IO) {
        emit(MoviesRepository.getMovie(movieId))
    }

    fun getTvShow(tvShowId: Int) = liveData(Dispatchers.IO) {
        emit(TVShowsRepository.getTvShow(tvShowId))
    }

    override fun onCleared() {
        super.onCleared()
        moviesPaginator.cancelLoading()
        tvShowsPaginator.cancelLoading()
        peoplePaginator.cancelLoading()
    }
}