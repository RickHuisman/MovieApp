package com.rickh.movieapp.ui.people

import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.tmdb.PeopleRepository

class PopularPeoplePaginator : Paginator<PersonFind>() {
    override fun loadData() {
        val result = PeopleRepository.getPopular(pageIndex)
        items.postValue(getItemsForDisplay(result))
    }
}