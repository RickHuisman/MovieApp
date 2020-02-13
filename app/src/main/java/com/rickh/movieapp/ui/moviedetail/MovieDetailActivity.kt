package com.rickh.movieapp.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.omertron.themoviedbapi.model.credits.MediaCreditCast
import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.rickh.movieapp.R
import com.rickh.movieapp.data.tmdb.Result
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)

        val movieId = intent.getLongExtra(INTENT_MOVIE_ID, DEF_VALUE)
        if (movieId != DEF_VALUE) {
            loadMovie(movieId)
        }
    }

    private fun loadMovie(movieId: Long) {
        showLoading(true)
        viewModel.getMovieInfo(movieId).observe(this, Observer {
            when (it) {
                is Result.Success -> showMovieSuccess(it.data)
                is Result.Error -> showError()
            }
            showLoading(false)
        })
    }

    private fun showMovieSuccess(movieInfo: MovieInfo) {
        toolbar.title = movieInfo.title

        val backdrop = movieInfo.backdropPath

        Glide.with(backdrop_imageview)
            .load("https://image.tmdb.org/t/p/original/$backdrop")
            .into(backdrop_imageview)

        setOverview(movieInfo.overview)
        setCastList(movieInfo.cast)

        content.visibility = View.VISIBLE
    }

    private fun setOverview(overview: String) {
        overview_textview.text = overview
        overview_container.setOnClickListener { overview_textview.toggle() }
    }

    private fun setCastList(cast: List<MediaCreditCast>) {
        cast_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CastCreditsAdapter(cast)
            isNestedScrollingEnabled = false
        }
    }

    private fun showError() {
        // TODO Show error state
        content.visibility = View.INVISIBLE
    }

    private fun showLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        private const val DEF_VALUE = -1L
        private const val INTENT_MOVIE_ID = "MOVIE_ID"

        fun newIntent(context: Context, movieId: Long): Intent {
            val intent = Intent(context, MovieDetailActivity()::class.java)
            intent.putExtra(INTENT_MOVIE_ID, movieId)
            return intent
        }
    }
}