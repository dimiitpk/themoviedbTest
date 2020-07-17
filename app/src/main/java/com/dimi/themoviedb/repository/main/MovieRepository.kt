package com.dimi.themoviedb.repository.main

import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.models.Video
import com.dimi.themoviedb.ui.main.movies.state.MovieViewState
import com.dimi.themoviedb.utils.DataState
import com.dimi.themoviedb.utils.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope // check ?
interface MovieRepository {

    fun searchMovies(
        query: String,
        page: Int,
        genre: Int,
        stateEvent: StateEvent
    ): Flow<DataState<MovieViewState>>

    fun getMovieVideos(
        movieId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<MovieViewState>>

    fun getMovieDetails(
        movieId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<MovieViewState>>
}