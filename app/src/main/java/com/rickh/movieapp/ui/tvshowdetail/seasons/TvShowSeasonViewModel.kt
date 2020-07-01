package com.rickh.movieapp.ui.tvshowdetail.seasons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rickh.movieapp.data.tmdb.TVShowsRepository
import kotlinx.coroutines.Dispatchers

class TvShowSeasonViewModel : ViewModel() {

    fun getSeasonInfo(tvShowId: Int, seasonNumber: Int) = liveData(Dispatchers.IO) {
        emit(TVShowsRepository.getSeasonInfo(tvShowId, seasonNumber))
    }
}
