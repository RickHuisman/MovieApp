package com.rickh.movieapp.tmdb

import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.people.PersonPeople

object PeopleRepository {

    private val tmdbApi = TmdbApi("ea75e60dfc33c0ff92d6e331c9debbea").people

    fun getPopular(pageIndex: Int): List<PersonPeople> {
        return tmdbApi.getPersonPopular(pageIndex).results
    }

    fun getPersonInfo(personId: Int): PersonPeople {
        return tmdbApi.getPersonInfo(personId)
    }
}