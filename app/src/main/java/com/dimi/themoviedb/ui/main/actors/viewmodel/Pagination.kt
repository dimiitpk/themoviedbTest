package com.dimi.themoviedb.ui.main.actors.viewmodel

import android.util.Log
import com.dimi.themoviedb.ui.main.actors.state.ActorStateEvent.*
import com.dimi.themoviedb.ui.main.actors.state.ActorViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.actorFields.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.refreshFromCache(clearLayoutManagerState: Boolean = false) {

    if (!isJobAlreadyActive(SearchActors())) {
        setQueryExhausted(false)
        setStateEvent(SearchActors(clearLayoutManagerState))
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.loadFirstPage() {
    if (!isJobAlreadyActive(SearchActors())) {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(SearchActors())
        Log.e(TAG, "MovieViewModel: loadFirstPage: ${viewState.value!!.actorFields.searchQuery}")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun ActorViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().actorFields.page ?: 1// get current page
    update.actorFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.nextPage() {
    if (!isJobAlreadyActive(SearchActors())
        && !viewState.value!!.actorFields.isQueryExhausted!!
    ) {
        Log.d(TAG, "MovieViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(SearchActors())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.handleIncomingMovieListData(viewState: ActorViewState) {

    viewState.actorFields.let { movieFields ->
        movieFields.actorList?.let { setActorDataList(it) }
    }
}


