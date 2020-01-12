package com.rickh.movieapp.ui.persondetail

interface FilterChangedCallback {
    fun onFilterChanged(filter: String, active: Boolean)
}