package com.rickh.movieapp.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rickh.movieapp.data.tmdb.MoviesRepository
import kotlinx.coroutines.Dispatchers

class MovieDetailViewModel : ViewModel() {

    fun getMovieInfo(movieId: Long) = liveData(Dispatchers.IO) {
        emit(MoviesRepository.getMovieInfo(movieId))
    }
}