package com.rickh.movieapp.ui.movies

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.MainActivity
import com.rickh.movieapp.ui.widgets.PopupWindowWithMaterialTransition
import info.movito.themoviedbapi.model.MovieDb
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MovieDetailPopup(private val activity: Activity, private val movieId: Long) :
    PopupWindowWithMaterialTransition(activity) {

    private val viewModel: MoviesViewModel

    private lateinit var titleTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var yearReleasedTextView: TextView
    private lateinit var runtimeTextView: TextView

    init {
        val popupView = LayoutInflater.from(activity).inflate(R.layout.popup_movie_detail, null)
        contentView = popupView

        viewModel = ViewModelProviders.of(activity as MainActivity).get(MoviesViewModel::class.java)
        initViewBindings()
        initViewModelObservers()
    }

    override fun calculateTransitionEpicenter(
        anchor: View,
        popupDecorView: ViewGroup,
        showLocation: Point
    ): Rect {
        return Rect(showLocation.x, showLocation.y, showLocation.x, showLocation.y)
    }

    private fun initViewBindings() {
        titleTextView = contentView.findViewById(R.id.moviedetailpopup_title)
        ratingTextView = contentView.findViewById(R.id.moviedetailpopup_rating)
        yearReleasedTextView = contentView.findViewById(R.id.moviedetailpopup_year_released)
        runtimeTextView = contentView.findViewById(R.id.moviedetailpopup_runtime)
    }

    private fun initViewModelObservers() {
        viewModel.getMovie(movieId).observe(activity as MainActivity, Observer {
            renderMovieDetail(it)
        })
    }

    private fun renderMovieDetail(movie: MovieDb) {
        titleTextView.text = movie.title
        ratingTextView.text = movie.voteAverage.toString()
        yearReleasedTextView.text = getReleaseDate(movie.releaseDate)
        runtimeTextView.text = constructRuntime(movie.runtime)
    }

    private fun getReleaseDate(releaseDate: String): String {
        return LocalDate.parse(
            releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ).year.toString()
    }

    private fun constructRuntime(runtime: Int): String {
        val hours = runtime / 60
        val minutes = runtime % 60

        return when {
            // only minutes
            hours < 1 -> activity.resources.getString(
                R.string.popup_movie_detail_runtime_in_minutes,
                minutes
            )
            // only hours
            minutes == 0 -> activity.resources.getString(
                R.string.popup_movie_detail_runtime_in_hours,
                hours
            )
            // hours ans minutes
            else -> activity.resources.getString(
                R.string.popup_movie_detail_runtime_in_hours_and_minutes,
                hours,
                minutes
            )
        }
    }
}