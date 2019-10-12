package com.rickh.movieapp.ui.people

import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.tmdb.PeopleRepository
import kotlinx.coroutines.launch

class PopularPeoplePaginator : Paginator<PersonFind>() {
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
        oldItems: List<PersonFind>,
        newItems: List<PersonFind>
    ): List<PersonFind> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }
}