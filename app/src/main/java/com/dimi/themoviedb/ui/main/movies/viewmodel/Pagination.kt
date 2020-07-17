package com.dimi.themoviedb.ui.main.movies.viewmodel

import android.util.Log
import com.dimi.themoviedb.ui.main.movies.state.MovieStateEvent
import com.dimi.themoviedb.ui.main.movies.state.MovieStateEvent.*
import com.dimi.themoviedb.ui.main.movies.state.MovieViewState
import com.dimi.themoviedb.ui.main.movies.viewmodel.MovieViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.movieFields.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.refreshFromCache(clearLayoutManagerState: Boolean = false) {

    if (!isJobAlreadyActive(SearchMovies())) {
        setQueryExhausted(false)
        setStateEvent(SearchMovies(clearLayoutManagerState))
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.loadFirstPage() {
    if (!isJobAlreadyActive(SearchMovies())) {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(SearchMovies())
        Log.e(TAG, "MovieViewModel: loadFirstPage: ${viewState.value!!.movieFields.searchQuery}")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun MovieViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().movieFields.page ?: 1// get current page
    update.movieFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.nextPage() {
    if (!isJobAlreadyActive(SearchMovies())
        && !viewState.value!!.movieFields.isQueryExhausted!!
    ) {
        Log.d(TAG, "MovieViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(SearchMovies())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.handleIncomingMovieListData(viewState: MovieViewState) {

    viewState.movieFields.let { movieFields ->
        movieFields.movieList?.let { setMovieListData(it) }
    }
}


