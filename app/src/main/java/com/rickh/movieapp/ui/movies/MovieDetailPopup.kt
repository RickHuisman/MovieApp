package com.rickh.movieapp.ui.movies

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.widgets.PopupWindowWithMaterialTransition


class MovieDetailPopup(context: Context) : PopupWindowWithMaterialTransition(context) {

    init {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_movie_detail, null)
        contentView = popupView
    }

    override fun calculateTransitionEpicenter(
        anchor: View,
        popupDecorView: ViewGroup,
        showLocation: Point
    ): Rect {
        return Rect(showLocation.x, showLocation.y, showLocation.x, showLocation.y)
    }
}