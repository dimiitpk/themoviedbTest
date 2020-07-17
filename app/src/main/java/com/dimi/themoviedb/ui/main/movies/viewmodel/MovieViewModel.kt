package com.dimi.themoviedb.ui.main.movies.viewmodel

import android.util.Log
import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.repository.main.MovieRepositoryImpl
import com.dimi.themoviedb.ui.BaseViewModel
import com.dimi.themoviedb.ui.main.movies.state.MovieStateEvent
import com.dimi.themoviedb.ui.main.movies.state.MovieStateEvent.*
import com.dimi.themoviedb.ui.main.movies.state.MovieViewState
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
class MovieViewModel
@Inject
constructor(
    private val movieRepository: MovieRepositoryImpl
) : BaseViewModel<MovieViewState>() {

    // take new incoming data and set so can be observe to fragments/activities
    override fun handleNewData(data: MovieViewState) {

        data.movieFields.let { movieFields ->

            movieFields.movieList?.let {
                handleIncomingMovieListData(data)
            }
            movieFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhausted(isQueryExhausted)
            }
        }

        data.viewMovieFields.let { viewMovieFields ->
            viewMovieFields.movie?.let { movie ->
                setViewMovie(movie)
            }
            viewMovieFields.castList?.let { castList ->
                setMovieCastList(castList)
            }
            Log.d("MRKNASMOGA3", viewMovieFields.videoList.toString())
            viewMovieFields.videoList?.let { videoList ->
                Log.d("MRKNASMOGA2", videoList.toString())
                setMovieVideoList(videoList)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if (!isJobAlreadyActive(stateEvent)) {
            val job: Flow<DataState<MovieViewState>> = when (stateEvent) {

                is SearchMovies -> {
                    if (stateEvent.clearLayoutManagerState) {
                        clearLayoutManagerState()
                    }
                    movieRepository.searchMovies(
                        stateEvent = stateEvent,
                        query = getSearchQuery(),
                        page = getPage(),
                        genre = getGenre()
                    )
                }
                is GetMovieDetails -> {
                    movieRepository.getMovieDetails(
                        movieId = getClickedMovieId(),
                        stateEvent = stateEvent
                    )
                }
                is GetMovieVideos -> {
                    movieRepository.getMovieVideos(
                        movieId = getClickedMovieId(),
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

    override fun initNewViewState(): MovieViewState {
        return MovieViewState()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}