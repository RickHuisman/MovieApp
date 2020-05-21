package com.rickh.movieapp.ui.tvshowdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rickh.movieapp.data.tmdb.TVShowsRepository
import kotlinx.coroutines.Dispatchers

class TvShowSeasonViewModel : ViewModel() {

    // TODO Duplicate in TvShowViewModel
//    fun getTvShowInfo(tvShowId: Long) = liveData(Dispatchers.IO) {
//        emit(TVShowsRepository.getTvShowInfo(tvShowId))
//    }

    fun getSeasonInfo(tvShowId: Int, seasonNumber: Int) = liveData(Dispatchers.IO) {
        emit(TVShowsRepository.getSeasonInfo(tvShowId, seasonNumber))
    }
}
