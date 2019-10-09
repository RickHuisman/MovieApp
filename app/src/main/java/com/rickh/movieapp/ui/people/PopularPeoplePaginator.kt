package com.rickh.movieapp.ui.people

import com.rickh.movieapp.tmdb.PeopleRepository
import info.movito.themoviedbapi.model.people.PersonPeople
import kotlinx.coroutines.launch

class PopularPeoplePaginator : Paginator<PersonPeople>() {
    override fun loadMore() {
        scope.launch {
            loadingProgress.postValue(true)

            val oldItems = items.value.orEmpty()
            val newItems = PeopleRepository.getPopular(pageIndex)
            items.postValue(getItemsForDisplay(oldItems, newItems))

            pageIndex++
            loadingProgress.postValue(false)
        }
    }

    private fun getItemsForDisplay(
        oldItems: List<PersonPeople>,
        newItems: List<PersonPeople>
    ): List<PersonPeople> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }
}