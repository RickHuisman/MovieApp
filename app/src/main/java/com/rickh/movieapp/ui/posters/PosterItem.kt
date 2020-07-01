package com.rickh.movieapp.ui.posters

import com.omertron.themoviedbapi.enumeration.MediaType

class PosterItem(
    val id: Long,
    val posterPath: String,
    var mediaType: MediaType
) {
    var hasFadedIn = false
}