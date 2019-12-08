package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.credits.CreditMovieBasic
import com.omertron.themoviedbapi.model.person.PersonCreditList
import com.omertron.themoviedbapi.model.person.PersonFind
import com.omertron.themoviedbapi.model.person.PersonInfo

object PeopleRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPopular(pageIndex: Int): Result<List<PersonFind>> {
        return try {
            val result = tmdbApi.getPersonPopular(pageIndex).results
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getPersonInfo(personId: Int): Result<PersonInfo> {
        return try {
            val result = tmdbApi.getPersonInfo(personId)
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }

    fun getCredits(personId: Int): Result<PersonCreditList<CreditBasic>> {
        return try {
            val result = tmdbApi.getPersonCombinedCredits(personId, "en")
            Result.Success(result)
        } catch (e: MovieDbException) {
            Result.Error(e)
        }
    }
}