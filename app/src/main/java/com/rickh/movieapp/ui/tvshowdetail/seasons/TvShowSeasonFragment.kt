package com.rickh.movieapp.ui.tvshowdetail.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.rickh.movieapp.R
import com.rickh.movieapp.data.tmdb.Result
import kotlinx.android.synthetic.main.activity_tv_show_detail.*
import kotlinx.android.synthetic.main.fragment_tv_show_season.*
import timber.log.Timber

class TvShowSeasonFragment : Fragment() {

    private lateinit var viewModel: TvShowSeasonViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(TvShowSeasonViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val tvShowId = arguments!!.getInt(KEY_TV_SHOW_ID)
        val seasonNumber = arguments!!.getInt(KEY_SEASON_NUMBER)

        Timber.d("$tvShowId - $seasonNumber")
        viewModel.getSeasonInfo(tvShowId, seasonNumber).observe(requireActivity(), Observer {
            when (it) {
                is Result.Success -> {
                    val episodes = it.data.episodes
                    if (episodes != null) setupList(episodes)
                }
                is Result.Error -> Timber.d("$it")
            }
        })
    }

    private fun setupList(episodes: List<TVEpisodeInfo>) {
        with(episodes_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = EpisodesAdapter(episodes)
        }
    }

    companion object {
        private const val KEY_TV_SHOW_ID = "TV_SHOW_ID"
        private const val KEY_SEASON_NUMBER = "SEASON_NUMBER"

        fun create(tvShowId: Int, seasonNumber: Int): TvShowSeasonFragment {
            return TvShowSeasonFragment()
                .apply {
                    val bundle = Bundle(2)
                    bundle.putSerializable(KEY_TV_SHOW_ID, tvShowId)
                    bundle.putSerializable(KEY_SEASON_NUMBER, seasonNumber)
                    arguments = bundle
                }
        }
    }
}