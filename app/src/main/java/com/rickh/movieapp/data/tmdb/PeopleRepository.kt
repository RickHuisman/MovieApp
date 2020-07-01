package com.rickh.movieapp.data.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.ui.persondetail.PersonInfoAndCredits

object PeopleRepository {

    private val tmdbApi = TheMovieDbApi.TheMovieDbApi

    fun getPopular(pageIndex: Int): Result<List<PersonFind>> {
        return try {
            Result.Success(tmdbApi.getPersonPopular(pageIndex).results)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getPersonInfoAndCredits(personId: Int): Result<PersonInfoAndCredits> {
        return try {
            val info = tmdbApi.getPersonInfo(personId)
            val credits = tmdbApi.getPersonCombinedCredits(personId, "en")

            Result.Success(PersonInfoAndCredits(info, credits))
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}