package com.rickh.movieapp.ui.persondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.person.PersonCreditList
import com.omertron.themoviedbapi.model.person.PersonInfo
import com.rickh.movieapp.tmdb.PeopleRepository
import kotlinx.coroutines.Dispatchers

class PersonDetailViewModel : ViewModel() {

    fun getPerson(personId: Int) = liveData(Dispatchers.IO) {
        emit(PeopleRepository.getPersonInfoAndCredits(personId))
    }
}

data class PersonInfoAndCredits(
    val personInfo: PersonInfo,
    val credits: PersonCreditList<CreditBasic>
)