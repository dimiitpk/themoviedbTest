package com.dimi.themoviedb.ui.main.movies.state

import com.dimi.themoviedb.utils.StateEvent

const val ERROR_MOVIE_SEARCH = "Error searching movies."
const val ERROR_VIDEO_SEARCH = "Error searching for videos."
const val ERROR_CAST_SEARCH = "Error searching for actors."
const val ERROR_DETAILS_SEARCH = "Error getting movie details."
sealed class MovieStateEvent : StateEvent {

    class SearchMovies(
        val clearLayoutManagerState: Boolean = true
    ): MovieStateEvent() {
        override fun errorInfo(): String {
            return ERROR_MOVIE_SEARCH
        }

        override fun toString(): String {
            return "SearchMovies"
        }
    }

    class GetMovieDetails: MovieStateEvent() {
        override fun errorInfo(): String {
            return ERROR_DETAILS_SEARCH
        }

        override fun toString(): String {
            return "GetMovieDetails"
        }
    }

    class GetMovieVideos: MovieStateEvent() {
        companion object {
            var isEventCalled: Boolean = false
        }

        override fun errorInfo(): String {
            return ERROR_VIDEO_SEARCH
        }

        override fun toString(): String {
            return "GetMovieVideos"
        }
    }

    class None: MovieStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

        override fun toString(): String {
            return "None"
        }
    }
}