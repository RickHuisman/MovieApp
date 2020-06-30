package com.rickh.movieapp.ui.posters

import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omertron.themoviedbapi.model.media.MediaBasic
import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.R
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.ui.HomeActivity
import com.rickh.movieapp.ui.posters.Category.MOVIES
import com.rickh.movieapp.ui.posters.Category.TV_SHOWS
import com.rickh.movieapp.ui.widgets.PopupWindowWithMaterialTransition
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class PosterDetailPopup(
    private val activity: Activity,
    private val category: Category,
    private val movieId: Long,
    private val textColor: String
) :
    PopupWindowWithMaterialTransition(activity) {

    private val viewModel: HomeViewModel

    private lateinit var popupContainer: RelativeLayout
    private lateinit var titleTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var yearReleasedTextView: TextView
    private lateinit var runtimeTextView: TextView

    init {
        val popupView = LayoutInflater.from(activity).inflate(R.layout.popup_movie_detail, null)
        contentView = popupView

        viewModel = ViewModelProvider(activity as HomeActivity).get(HomeViewModel::class.java)

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
        popupContainer = contentView.findViewById(R.id.moviedetailpopup_container)
        titleTextView = contentView.findViewById(R.id.moviedetailpopup_title)
        ratingTextView = contentView.findViewById(R.id.moviedetailpopup_rating)
        yearReleasedTextView = contentView.findViewById(R.id.moviedetailpopup_year_released)
        runtimeTextView = contentView.findViewById(R.id.moviedetailpopup_runtime)
    }

    private fun initViewModelObservers() {
        when (category) {
            MOVIES -> {
                viewModel.getMovie(movieId.toInt()).observe(activity as HomeActivity, Observer {
                    when (it) {
                        is Result.Success -> renderPopupDetail(it.data)
                        is Result.Error -> Timber.d(it.exception)
                    }
                })
            }
            TV_SHOWS -> {
                viewModel.getTvShow(movieId.toInt()).observe(activity as HomeActivity, Observer {
                    when (it) {
                        is Result.Success -> renderPopupDetail(it.data)
                        is Result.Error -> Timber.d(it.exception)
                    }
                })
            }
        }
    }

    private fun renderPopupDetail(media: MediaBasic) {
        when (category) {
            MOVIES -> {
                media as MovieInfo
                titleTextView.text = media.title
//                titleTextView.setTextColor(Color.parseColor(textColor))
                popupContainer.background.setColorFilter(Color.parseColor(textColor), PorterDuff.Mode.SRC_ATOP)

                ratingTextView.text = media.voteAverage.toString()
                yearReleasedTextView.text = getReleaseDate(media.releaseDate)
                runtimeTextView.text = constructRuntime(media.runtime)
            }
            TV_SHOWS -> {
                media as TVInfo
                titleTextView.text = media.name
                ratingTextView.text = media.voteAverage.toString()
                yearReleasedTextView.text = getReleaseDate(media.firstAirDate)
//                runtimeTextView.text = constructRuntime(media.)
            }
        }
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