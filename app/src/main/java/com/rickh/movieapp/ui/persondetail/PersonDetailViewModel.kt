package com.rickh.movieapp.ui.persondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rickh.movieapp.tmdb.PeopleRepository
import kotlinx.coroutines.Dispatchers

class PersonDetailViewModel : ViewModel() {

    fun getPerson(personId: Int) = liveData(Dispatchers.IO) {
        emit(PeopleRepository.getPersonInfoAndCredits(personId))
    }
}