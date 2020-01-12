package com.rickh.movieapp.ui.persondetail

import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.person.PersonCreditList
import com.omertron.themoviedbapi.model.person.PersonInfo

data class PersonInfoAndCredits(
    val personInfo: PersonInfo,
    val credits: PersonCreditList<CreditBasic>
)