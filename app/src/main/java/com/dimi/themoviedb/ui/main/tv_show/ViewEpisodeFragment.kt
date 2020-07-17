package com.dimi.themoviedb.ui.main.tv_show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.dimi.themoviedb.R
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.TVEpisode
import com.dimi.themoviedb.utils.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_view_episode.*
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
@FlowPreview
class ViewEpisodeFragment : BaseTVShowFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        uiCommunicationListener.expandAppBar()

      //  if( isObjectNotPassedThroughNavigation() ) {

            nested_scroll_episode.visibility = View.VISIBLE
            subscribeObservers()
       // }
    }


    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {

                viewState.viewEpisodeFields.tvEpisode?.let { episode ->
                    setTvEpisodeProperties(episode)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {

                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    private fun setTvEpisodeProperties(episode: TVEpisode) {

        dependencyProvider.getGlideRequestManager()
            .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + episode.stillPath)
            .into(episode_image)


        episode_name.text = episode.name
        episode_air_date.text = String.format("Air date: %s", episode.airDate)
        episode_number.text = String.format("Season %d -> Episode %d", episode.seasonNumber, episode.episodeNumber)
        episode_overview.text = episode.overview
    }
}