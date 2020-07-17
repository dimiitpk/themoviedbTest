package com.dimi.themoviedb.ui.main.actors.state

import com.dimi.themoviedb.utils.StateEvent

const val ERROR_ACTOR_SEARCH = "Error searching actors."
const val ERROR_ACTOR_DETAILS = "Error searching for actor details."
const val ERROR_ACTOR_CREDITS = "Error searching for actor credits."
sealed class ActorStateEvent : StateEvent {

    class SearchActors(
        val clearLayoutManagerState: Boolean = true
    ): ActorStateEvent() {
        override fun errorInfo(): String {
            return ERROR_ACTOR_SEARCH
        }

        override fun toString(): String {
            return "SearchActors"
        }
    }

    class ActorCredits: ActorStateEvent() {
        override fun errorInfo(): String {
            return ERROR_ACTOR_CREDITS
        }

        override fun toString(): String {
            return "ActorCredits"
        }
    }

    class ActorDetails: ActorStateEvent() {
        override fun errorInfo(): String {
            return ERROR_ACTOR_DETAILS
        }

        override fun toString(): String {
            return "ActorDetails"
        }
    }

    class None: ActorStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

        override fun toString(): String {
            return "None"
        }
    }
}