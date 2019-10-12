package com.rickh.movieapp.tmdb

import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.person.PersonFind
import com.omertron.themoviedbapi.model.person.PersonInfo

object PeopleRepository {

    private val tmdbApi = TheMovieDbApi("ea75e60dfc33c0ff92d6e331c9debbea")

    fun getPopular(pageIndex: Int): List<PersonFind> {
        return tmdbApi.getPersonPopular(pageIndex).results
    }

    fun getPersonInfo(personId: Int): PersonInfo {
        return tmdbApi.getPersonInfo(personId)
    }
}