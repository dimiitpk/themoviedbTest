package com.dimi.themoviedb.ui.main.actors.viewmodel

import android.os.Parcelable
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.models.CombinedCredit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().actorFields.searchQuery
        ?: ""
}


@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().actorFields.page
        ?: 1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getLayoutManagerState(): Parcelable? {
    getCurrentViewStateOrNew().let {
        return it.actorFields.layoutManagerState
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getClickedActorId(): Int {
    return getCurrentViewStateOrNew().actorFields.clickedActorId
        ?: return -1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getActorId(): Int {
    return getCurrentViewStateOrNew().actorViewFields.actor?.id
        ?: return -1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getActorLoadedBeforePassing(): Actor? {
    return getCurrentViewStateOrNew().actorViewFields.actorLoadedBeforePassing
        ?: return null
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getActor(): Actor? {
    return getCurrentViewStateOrNew().actorViewFields.actor
        ?: return null
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.getActorCreditList(): List<CombinedCredit>? {
    return getCurrentViewStateOrNew().actorViewFields.creditsList
        ?: return null
}
