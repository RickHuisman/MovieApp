package com.rickh.movieapp.data.tmdb

import com.omertron.themoviedbapi.MovieDbException
import com.omertron.themoviedbapi.model.person.PersonFind
import com.omertron.themoviedbapi.model.person.PersonInfo

object PeopleRepository {

    private val tmdbApi = TheMovieDbApi.TheMovieDbApi

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
}