package com.dimi.themoviedb.repository.main

import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState
import com.dimi.themoviedb.utils.DataState
import com.dimi.themoviedb.utils.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope // check ?
interface TVShowRepository {

    fun searchTVShows(
        query: String,
        page: Int,
        genre: Int,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>>

    fun getTvShowVideos(
        tvShowId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>>

    fun getTvSeasonEpisodes(
        tvShowId: Int,
        seasonNumber: Int,
        tvSeasonId: Long,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>>

    fun getTvShowDetails(
        tvShowId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>>
}