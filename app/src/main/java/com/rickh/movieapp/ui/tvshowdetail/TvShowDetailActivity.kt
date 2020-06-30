package com.rickh.movieapp.ui.tvshowdetail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.R
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.ui.posters.PosterTarget
import kotlinx.android.synthetic.main.activity_tv_show_detail.*

class TvShowDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: TvShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)

        viewModel = ViewModelProvider(this).get(TvShowViewModel::class.java)

        val tvShowId = intent.getLongExtra(INTENT_TV_SHOW_ID, DEF_VALUE)
        if (tvShowId != DEF_VALUE) {
            loadTvShow(87108) // TODO load tvShowId
        }
    }

    private fun loadTvShow(tvShowId: Long) {
        showLoading(true)
        viewModel.getTvShowInfo(tvShowId.toInt()).observe(this, Observer {
            when (it) {
                is Result.Success -> showTvShowInfo(it.data)
                is Result.Error -> showError()
            }
            showLoading(false)
        })
    }

    private fun showTvShowInfo(tvShowInfo: TVInfo) {
        toolbar.title = tvShowInfo.name

        setPoster(tvShowInfo)
        setOverview(tvShowInfo.overview)

        // TODO Remove
//        episodes.setOnClickListener {
//            val intent = TvShowSeasonActivity.newIntent(this, tvShowInfo)
//            startActivity(intent)
//        }

        content.visibility = View.VISIBLE
    }

    private fun setPoster(tvShowInfo: TVInfo) {
        return
        Glide.with(this)
            .load(
                this.getString(R.string.tmdb_base_img_url, tvShowInfo.posterPath)
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ) = false

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // TODO
//                    if (!item.hasFadedIn) {
//                        fade()
//                        item.hasFadedIn = true
//                    }
                    return false
                }
            })
//            .placeholder(placeholder) TODO
//            .diskCacheStrategy(DiskCacheStrategy.DATA) TODO
//            .centerCrop() TODO
//            .transition(DrawableTransitionOptions.withCrossFade()) TODO
//            .into(PosterTarget(poster)) TODO
            .into(poster)
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