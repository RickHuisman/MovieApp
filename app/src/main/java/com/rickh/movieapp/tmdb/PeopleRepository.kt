package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.person.PersonCreditList
import com.omertron.themoviedbapi.model.person.PersonFind
import com.omertron.themoviedbapi.model.person.PersonInfo
import com.rickh.movieapp.ui.persondetail.PersonInfoAndCredits

object PeopleRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPersonInfoAndCredits(personId: Int): Result<PersonInfoAndCredits> {
        return try {
            val info = tmdbApi.getPersonInfo(personId)
            val credits = tmdbApi.getPersonCombinedCredits(personId, "en")

            Result.Success(PersonInfoAndCredits(info, credits))
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getPopular(pageIndex: Int): Result<List<PersonFind>> {
        return try {
            val result = tmdbApi.getPersonPopular(pageIndex).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}