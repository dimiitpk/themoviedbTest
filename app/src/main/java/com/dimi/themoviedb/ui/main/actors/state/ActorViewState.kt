package com.dimi.themoviedb.ui.main.actors.state

import android.os.Parcelable
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.models.CombinedCredit
import kotlinx.android.parcel.Parcelize

const val ACTOR_VIEW_STATE_BUNDLE_KEY = "com.dimi.themoviedb.ui.main.actors.state.ActorViewState"

@Parcelize
data class ActorViewState (

    var actorFields: ActorFields = ActorFields(),

    var actorViewFields: ActorViewFields = ActorViewFields()

): Parcelable {

    @Parcelize
    data class ActorFields(
        var actorList: List<Actor>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var layoutManagerState: Parcelable? = null,
        var clickedActorId: Int? = null
    ) : Parcelable

    @Parcelize
    data class ActorViewFields(
        var actorLoadedBeforePassing: Actor? = null,
        var actor: Actor? = null,
        var creditsList: List<CombinedCredit>? = null
    ): Parcelable
}