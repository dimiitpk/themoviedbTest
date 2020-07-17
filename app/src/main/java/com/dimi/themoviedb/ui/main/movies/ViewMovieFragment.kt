package com.dimi.themoviedb.ui.main.movies

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.models.Video
import com.dimi.themoviedb.ui.main.movies.adapters.CastListAdapter
import com.dimi.themoviedb.ui.main.movies.state.ERROR_VIDEO_SEARCH
import com.dimi.themoviedb.ui.main.movies.state.MovieStateEvent.*
import com.dimi.themoviedb.ui.main.movies.viewmodel.*
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.utils.SpacesItemDecoration
import com.dimi.themoviedb.utils.StateMessageCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.fragment_view_movie.*
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
@FlowPreview
class ViewMovieFragment : BaseMovieFragment(),
    CastListAdapter.Interaction {

    private var videoNamesList = mutableListOf<String>()
    private var videoIdToPlay: String? = null
    private var youtubePlayerView: YouTubePlayerView? = null
    private var youtubePlayer: YouTubePlayer? = null
    private lateinit var recyclerAdapter: CastListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        uiCommunicationListener.expandAppBar() // if u are scrolled to middle myb on last fragment then toolbar will not be shown probably
        youtubePlayerView = view.findViewById(R.id.movie_trailer)

        getMovieDetails()

        initRecyclerView()
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

                viewState.viewMovieFields.movie?.let { movie ->
                    setMovieProperties(movie)
                }
                viewState.viewMovieFields.videoList?.let { list ->
                    if (list.isNotEmpty()) {
                        setListOfVideos(list)
                    }
                }
                viewState.viewMovieFields.castList?.let { list ->
                    if (list.isNotEmpty()) {
                        recyclerAdapter.apply {
                            preloadGlideImages(
                                requestManager = dependencyProvider.getGlideRequestManager(),
                                list = list
                            )
                            submitList(
                                viewState.viewMovieFields.castList
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

                videoIdToPlay?.let { videoId ->
                    showImageView(false)
                    youtubePlayer!!.cueVideo(videoId, 0f)
                } ?: showImageView(true)
            }
        })
    }

    private fun initRecyclerView() {

        actors_recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                CastListAdapter(
                    dependencyProvider.getGlideRequestManager(),
                    this@ViewMovieFragment
                )
            adapter = recyclerAdapter
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

    private fun setMovieProperties(movie: Movie) {
        dependencyProvider.getGlideRequestManager()
            .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + movie.posterPath)
            .into(movie_image)

        movie_title.text = movie.title
        movie_release_date.text = String.format("Release date: %s", movie.releaseDate)
        movie_rating.text = String.format("Rating: %1$,.2f", movie.voteAverage)
        movie_overview.text = movie.overview

        if( movie_overview.text.isNotBlank() && movie_title.text.isNotBlank() && viewModel.getMovieVideoList().isEmpty() && !GetMovieVideos.isEventCalled) {
            //loadVideos()
        }
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

                    Log.d(TAG, viewModel.getMovieVideoList()[position].key)
                    videoIdToPlay = viewModel.getMovieVideoList()[position].key
                    youtubePlayer?.cueVideo(videoIdToPlay!!, 0f)

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


    override fun onItemSelected(position: Int, item: Cast) {
        activity?.let {
            dependencyProvider.onClickedPassingObject(item.toActor())
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

        viewModel.clearMovieVideoList() // clearing movie video list, we need new every time we select a single move( prevent showing old then new list)
        videoNamesList.clear()
        youtubePlayerView?.release() // prevent memory leak ( myb i do not need to release it manually coz its attached to lifecycle but nvm )
        youtubePlayerView = null
        youtubePlayer = null
        videoIdToPlay = null
        actors_recycler_view.adapter = null
    }

    private fun getMovieDetails() {
        if( viewModel.getClickedMovieId() != viewModel.getViewMovie().id )
            viewModel.setStateEvent(
                GetMovieDetails()
            )
    }

    private fun loadVideos() {
        GetMovieVideos.isEventCalled = true
        viewModel.setStateEvent(
            GetMovieVideos()
        )
    }
}
