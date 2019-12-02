package com.rickh.movieapp.ui.persondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.omertron.themoviedbapi.model.person.PersonInfo
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.PeopleRepository
import com.rickh.movieapp.ui.people.PopularPeoplePaginator
import kotlinx.coroutines.Dispatchers

class PersonDetailViewModel : ViewModel() {

    fun getPersonInfo(personId: Int) = liveData(Dispatchers.IO) {
        emit(PeopleRepository.getPersonInfo(personId))
    }
}