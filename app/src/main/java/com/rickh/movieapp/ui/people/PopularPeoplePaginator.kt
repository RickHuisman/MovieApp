package com.rickh.movieapp.ui.people

import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.data.tmdb.PeopleRepository
import com.rickh.movieapp.data.tmdb.Result

class PopularPeoplePaginator : Paginator<PersonFind>() {
    override fun loadData(): Result<Any> {
        return PeopleRepository.getPopular(pageIndex)
    }
}