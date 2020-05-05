package com.rickh.movieapp.ui.tvshowdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.R
import com.rickh.movieapp.data.tmdb.Result
import kotlinx.android.synthetic.main.activity_tv_show_detail.*
import timber.log.Timber

class TvShowDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: TvShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)

        viewModel = ViewModelProviders.of(this).get(TvShowViewModel::class.java)

        val tvShowId = intent.getLongExtra(INTENT_TV_SHOW_ID, DEF_VALUE)
        if (tvShowId != DEF_VALUE) {
            loadTvShow(tvShowId)
        }
    }

    private fun loadTvShow(tvShowId: Long) {
        showLoading(true)
        viewModel.getTvShowInfo(tvShowId).observe(this, Observer {
            when (it) {
                is Result.Success -> showTvShowInfo(it.data)
                is Result.Error -> showError()
            }
            showLoading(false)
        })
    }

    private fun showTvShowInfo(tvShowInfo: TVInfo) {
        setOverview(tvShowInfo.overview)

        content.visibility = View.VISIBLE
    }

    private fun setOverview(overview: String) {
        overview_textview.text = overview
        overview_container.setOnClickListener { overview_textview.toggle() }
    }

    private fun showError() {
        // TODO
    }

    private fun showLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        private const val DEF_VALUE = -1L
        private const val INTENT_TV_SHOW_ID = "TV_SHOW_ID"

        fun newIntent(context: Context, tvShowId: Long): Intent {
            val intent = Intent(context, TvShowDetailActivity()::class.java)
            intent.putExtra(INTENT_TV_SHOW_ID, tvShowId)
            return intent
        }
    }
}