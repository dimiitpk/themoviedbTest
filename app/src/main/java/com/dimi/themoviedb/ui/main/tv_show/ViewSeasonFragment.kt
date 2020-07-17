package com.dimi.themoviedb.ui.main.tv_show

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimi.themoviedb.R
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.TVEpisode
import com.dimi.themoviedb.models.TVSeason
import com.dimi.themoviedb.ui.main.tv_show.adapters.TvEpisodeListAdapter
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowStateEvent
import com.dimi.themoviedb.ui.main.tv_show.viewmodel.clearTvShowEpisodeList
import com.dimi.themoviedb.ui.main.tv_show.viewmodel.setEpisode
import com.dimi.themoviedb.utils.SpacesItemDecoration
import com.dimi.themoviedb.utils.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_view_season.*
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
@FlowPreview
class ViewSeasonFragment : BaseTVShowFragment(),
    TvEpisodeListAdapter.Interaction{

    private lateinit var recyclerAdapter: TvEpisodeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        uiCommunicationListener.expandAppBar()

        //if( isObjectNotPassedThroughNavigation() ) {
            nested_scroll.visibility = View.VISIBLE
            initRecyclerViews()
            subscribeObservers()

            loadEpisodes()
       // }

        /*
        * setOnTouchListener listeners so you can scroll text in nestedScrollView parent
        * */
        nested_scroll.setOnTouchListener { _, _ ->
            season_overview.parent.requestDisallowInterceptTouchEvent(false)
            return@setOnTouchListener false
        }
        season_overview.setOnTouchListener { _, _ ->
            season_overview.parent.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false
        }
    }


    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {

                viewState.viewSeasonFields.tvSeason?.let { season ->
                    setTvSeasonProperties(season)
                }
                viewState.viewSeasonFields.episodeList?.let { list ->
                    if (list.isNotEmpty()) {
                        recyclerAdapter.apply {
                            preloadGlideImages(
                                requestManager = dependencyProvider.getGlideRequestManager(),
                                list = list
                            )
                            submitList(
                                viewState.viewSeasonFields.episodeList
                            )
                        }
                    }
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

    private fun initRecyclerViews() {

        episodes_recycler_view.apply {

            layoutManager = LinearLayoutManager(this@ViewSeasonFragment.context)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                TvEpisodeListAdapter(
                    dependencyProvider.getGlideRequestManager(),
                    this@ViewSeasonFragment
                )
            adapter = recyclerAdapter
        }
    }

    private fun setTvSeasonProperties( tvSeason: TVSeason ) {
        dependencyProvider.getGlideRequestManager()
            .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + tvSeason.posterPath)
            .into(season_image)

        tvSeason.overview?.let { overview ->
            if( overview.isNotBlank()) {
                season_overview.text = overview
                overview_container.visibility = View.VISIBLE
            }
        }

        // this line + scrollbars and maxLines in xml
        season_overview.movementMethod = ScrollingMovementMethod()
        season_air_date.text = String.format("Air date: %s", tvSeason.airDate)
        season_number.text = String.format("Season: %d", tvSeason.seasonNumber)
        season_overview.text = tvSeason.overview
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearTvShowEpisodeList()
        episodes_recycler_view.adapter = null
    }

    private fun loadEpisodes() {
        viewModel.setStateEvent(TVShowStateEvent.GetTVSeasonDetails())
    }

    override fun onItemSelected(position: Int, item: TVEpisode) {
        viewModel.setEpisode(item)
        findNavController().navigate(R.id.action_viewSeasonFragment_to_viewEpisodeFragment)
    }
}