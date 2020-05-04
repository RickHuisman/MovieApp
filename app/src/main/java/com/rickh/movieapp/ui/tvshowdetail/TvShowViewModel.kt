package com.rickh.movieapp.ui.tvshowdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rickh.movieapp.data.tmdb.TVShowsRepository
import kotlinx.coroutines.Dispatchers

class TvShowViewModel : ViewModel() {

    fun getTvShowInfo(tvShowId: Long) = liveData(Dispatchers.IO) {
        emit(TVShowsRepository.getTvShowInfo(tvShowId))
    }
}