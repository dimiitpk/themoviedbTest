package com.dimi.themoviedb.ui.main.tv_show.state

import com.dimi.themoviedb.utils.StateEvent

const val ERROR_TV_SHOW_SEARCH = "Error searching tv shows."
const val ERROR_TV_SHOW_DETAILS = "Error getting tv shows details."
const val ERROR_TV_SHOW_VIDEOS = "Error getting tv show videos."
const val ERROR_TV_SEASON_DETAILS = "Error getting season details."
sealed class TVShowStateEvent : StateEvent {

    class SearchTVShows(
        val clearLayoutManagerState: Boolean = true
    ): TVShowStateEvent() {
        override fun errorInfo(): String {
            return ERROR_TV_SHOW_SEARCH
        }

        override fun toString(): String {
            return "SearchTVShows"
        }
    }

    class GetTVSeasonDetails: TVShowStateEvent() {
        override fun errorInfo(): String {
            return ERROR_TV_SEASON_DETAILS
        }

        override fun toString(): String {
            return "GetTVSeasonDetails"
        }
    }

    class GetTVShowVideos: TVShowStateEvent() {
        override fun errorInfo(): String {
            return ERROR_TV_SHOW_VIDEOS
        }

        override fun toString(): String {
            return "GetTVShowVideos"
        }
    }

    class GetTVShowDetails: TVShowStateEvent() {
        override fun errorInfo(): String {
            return ERROR_TV_SHOW_DETAILS
        }

        override fun toString(): String {
            return "GetTVShowDetails"
        }
    }

    class None: TVShowStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

        override fun toString(): String {
            return "None"
        }
    }
}