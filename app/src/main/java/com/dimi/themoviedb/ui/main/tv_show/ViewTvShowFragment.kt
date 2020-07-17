package com.dimi.themoviedb.ui.main.tv_show

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Video
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.TVCast
import com.dimi.themoviedb.models.TVSeason
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.ui.main.tv_show.adapters.TvCastListAdapter
import com.dimi.themoviedb.ui.main.tv_show.adapters.TvSeasonListAdapter
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowStateEvent
import com.dimi.themoviedb.ui.main.tv_show.viewmodel.*
import com.dimi.themoviedb.utils.SpacesItemDecoration
import com.dimi.themoviedb.utils.StateMessageCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.fragment_view_movie.actors_recycler_view
import kotlinx.android.synthetic.main.fragment_view_movie.movie_image
import kotlinx.android.synthetic.main.fragment_view_movie.movie_overview
import kotlinx.android.synthetic.main.fragment_view_movie.movie_rating
import kotlinx.android.synthetic.main.fragment_view_movie.movie_release_date
import kotlinx.android.synthetic.main.fragment_view_movie.movie_title
import kotlinx.android.synthetic.main.fragment_view_movie.movie_trailer
import kotlinx.android.synthetic.main.fragment_view_tv_show.*
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
@FlowPreview
class ViewTvShowFragment : BaseTVShowFragment(),
    TvCastListAdapter.Interaction,
    TvSeasonListAdapter.Interaction {

    private var videoNamesList = mutableListOf<String>()
    lateinit var videoIdToPlay: String
    private var youtubePlayerView: YouTubePlayerView? = null
    private var youtubePlayer: YouTubePlayer? = null
    private lateinit var recyclerAdapter: TvCastListAdapter
    private lateinit var seasonsAdapter: TvSeasonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        uiCommunicationListener.expandAppBar() // if u are scrolled to middle myb on last fragment then toolbar will not be shown probably
        youtubePlayerView = view.findViewById(R.id.movie_trailer)
        getTvShowDetails()

        initRecyclerViews()
        subscribeObservers()

        loadVideos()

        movie_title.setOnClickListener {
            if (videoNamesList.isNotEmpty())
                showChooseVideoDialog(videoNamesList)
        }
    }


    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {

                viewState.viewTvShowFields.tvShow?.let { movie ->
                    setTvShowProperties(movie)
                }
                viewState.viewTvShowFields.videoList?.let { list ->
                    if (list.isNotEmpty()) {
                        setListOfVideos(list)
                    }
                }
                viewState.viewTvShowFields.castList?.let { list ->
                    if (list.isNotEmpty()) {
                        recyclerAdapter.apply {
                            preloadGlideImages(
                                requestManager = dependencyProvider.getGlideRequestManager(),
                                list = list
                            )
                            submitList(
                                viewState.viewTvShowFields.castList
                            )
                        }
                    }
                }
                viewState.viewTvShowFields.seasonList?.let { list ->
                    if (list.isNotEmpty()) {
                        seasonsAdapter.apply {
                            preloadGlideImages(
                                requestManager = dependencyProvider.getGlideRequestManager(),
                                list = list
                            )
                            submitList(
                                viewState.viewTvShowFields.seasonList
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

                if (stateMessage.response.message?.contains("Reason: Data is NULL")!!) {
                    viewModel.clearStateMessage()
                } else {
                    uiCommunicationListener.onResponseReceived(
                        response = it.response,
                        stateMessageCallback = object : StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })

        youtubePlayerView?.let {
            viewLifecycleOwner.lifecycle.addObserver(it)
        }

        youtubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer

                if (this@ViewTvShowFragment::videoIdToPlay.isInitialized) {
                    showImageView(false)
                    youtubePlayer!!.cueVideo(videoIdToPlay, 0f)
                } else
                    showImageView(true)
            }
        })
    }

    private fun initRecyclerViews() {

        actors_recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                TvCastListAdapter(
                    dependencyProvider.getGlideRequestManager(),
                    this@ViewTvShowFragment
                )
            adapter = recyclerAdapter
        }

        season_recycler_view.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            seasonsAdapter = TvSeasonListAdapter(
                dependencyProvider.getGlideRequestManager(),
                this@ViewTvShowFragment
            )
            adapter = seasonsAdapter
        }
    }

    private fun setListOfVideos(list: List<Video>) {
        if (list.isNotEmpty() && videoNamesList.isEmpty()) {
            videoIdToPlay = list[0].key // first from list going to view
            for (video in list) {
                videoNamesList.add(video.name)
            }
        }
    }

    private fun setTvShowProperties(tvShow: TVShow) {
        dependencyProvider.getGlideRequestManager()
            .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + tvShow.posterPath)
            .into(movie_image)

        movie_title.text = tvShow.title
        movie_release_date.text = String.format("First air date: %s", tvShow.firstAirDate)
        movie_rating.text = String.format("Rating: %1$,.2f", tvShow.voteAverage)
        movie_overview.text = tvShow.overview
    }

    private fun showChooseVideoDialog(list: List<String>) {

        activity?.let { activity ->
            val dialog = MaterialDialog(activity)
                .noAutoDismiss()
                .customView(R.layout.layout_videos_dialog)

            val view = dialog.getCustomView()


            view.findViewById<ListView>(R.id.video_list).apply {
                adapter = ArrayAdapter(activity, R.layout.layout_list_video_item, R.id.label, list)

                setOnItemClickListener { _, _, position, _ ->

                    Log.d(TAG, viewModel.getTvShowVideoList()[position].key)
                    videoIdToPlay = viewModel.getTvShowVideoList()[position].key
                    youtubePlayer?.cueVideo(videoIdToPlay, 0f)

                    showImageView(false)
                    dialog.dismiss()
                }
            }

            view.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }


    override fun onItemSelected(position: Int, item: TVCast) {
        activity?.let {
            dependencyProvider.onClickedPassingObject(item.toActor(), true)
        }
    }

    private fun showImageView(show: Boolean) {
        if (show) {
            movie_image.visibility = View.VISIBLE
            movie_trailer.visibility = View.GONE
        } else {
            movie_image.visibility = View.GONE
            movie_trailer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearTvShowVideoList()
        videoNamesList.clear()
        youtubePlayerView?.release() // prevent memory leak ( myb i do not need to release it manually coz its attached to lifecycle but nvm )
        youtubePlayerView = null
        youtubePlayer = null
        actors_recycler_view.adapter = null
        season_recycler_view.adapter = null
    }

    private fun getTvShowDetails() {
        if( viewModel.getClickedTvShowId() != viewModel.getTvShow()?.id)
            viewModel.setStateEvent(TVShowStateEvent.GetTVShowDetails())
    }

    private fun loadVideos() {
        viewModel.setStateEvent(TVShowStateEvent.GetTVShowVideos())
    }

    override fun onItemSelected(position: Int, item: TVSeason) {
        viewModel.setTvSeason(item)
        findNavController().navigate(R.id.action_viewTvShowFragment_to_viewSeasonFragment)
    }
}