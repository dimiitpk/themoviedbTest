package com.dimi.themoviedb.ui.main.tv_show.viewmodel

import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.repository.main.TVShowRepositoryImpl
import com.dimi.themoviedb.ui.BaseViewModel
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowStateEvent.*
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState
import com.dimi.themoviedb.utils.*
import com.dimi.themoviedb.utils.ErrorHandling.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@MainScope // check
class TVShowViewModel
@Inject
constructor(
    private val tvShowRepository: TVShowRepositoryImpl
) : BaseViewModel<TVShowViewState>() {

    // take new incoming data and set so can be observe to fragments/activities
    override fun handleNewData(data: TVShowViewState) {

        data.tvShowFields.let { movieFields ->

            movieFields.tvShowList?.let {
                handleIncomingMovieListData(data)
            }
            movieFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhausted(isQueryExhausted)
            }
        }

        data.viewTvShowFields.let { viewTVShowFields ->
            viewTVShowFields.tvShow?.let {
                setTvShow(it)
            }
            viewTVShowFields.castList?.let { castList ->
                setTvShowCastList(castList)
            }
            viewTVShowFields.videoList?.let { videoList ->
                setTvShowVideoList(videoList)
            }
            viewTVShowFields.seasonList?.let { seasons ->
                setTVSeasonList( seasons )
            }
        }

        data.viewSeasonFields.let { viewSeasonFields ->
            viewSeasonFields.episodeList?.let { episodeList ->
                setTVSeasonEpisodeList( episodeList )
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if (!isJobAlreadyActive(stateEvent)) {
            val job: Flow<DataState<TVShowViewState>> = when (stateEvent) {

                is SearchTVShows -> {
                    if (stateEvent.clearLayoutManagerState) {
                        clearLayoutManagerState()
                    }
                    tvShowRepository.searchTVShows(
                        stateEvent = stateEvent,
                        query = getSearchQuery(),
                        page = getPage(),
                        genre = getGenre()
                    )
                }
                is GetTVSeasonDetails -> {
                    tvShowRepository.getTvSeasonEpisodes(
                        tvShowId = getTvSeason().tvShowId,
                        seasonNumber = getTvSeason().seasonNumber,
                        tvSeasonId = getTvSeason().id,
                        stateEvent = stateEvent
                    )
                }
                is GetTVShowDetails -> {
                    tvShowRepository.getTvShowDetails(
                        tvShowId = getTvShowId(),
                        stateEvent = stateEvent
                    )
                }
                is GetTVShowVideos -> {
                    tvShowRepository.getTvShowVideos(
                        tvShowId = getTvShowId(),
                        stateEvent = stateEvent
                    )
                }
                else -> {
                    flow {
                        emit(
                            DataState.error(
                                response = Response(
                                    message = INVALID_STATE_EVENT,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        )
                    }
                }
            }
            launchJob(stateEvent, job)
        }
    }

    override fun initNewViewState(): TVShowViewState {
        return TVShowViewState()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}