package com.dimi.themoviedb.ui.main.tv_show.viewmodel

import android.util.Log
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowStateEvent
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowStateEvent.*
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.refreshFromCache(clearLayoutManagerState: Boolean = false) {

    if (!isJobAlreadyActive(SearchTVShows())) {
        setQueryExhausted(false)
        setStateEvent(SearchTVShows(clearLayoutManagerState))
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.loadFirstPage() {
    if (!isJobAlreadyActive(SearchTVShows())) {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(SearchTVShows())
        Log.e(TAG, "TVShowViewModel: loadFirstPage: ${viewState.value!!.tvShowFields.searchQuery}")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun TVShowViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().tvShowFields.page ?: 1// get current page
    update.tvShowFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.nextPage() {
    if (!isJobAlreadyActive(SearchTVShows())
        && !viewState.value!!.tvShowFields.isQueryExhausted!!
    ) {
        Log.d(TAG, "TVShowViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(SearchTVShows())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.handleIncomingMovieListData(viewState: TVShowViewState) {

    viewState.tvShowFields.let { tvShowFields ->
        tvShowFields.tvShowList?.let { setTVShowListData(it) }
    }
}


