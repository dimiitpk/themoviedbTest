package com.dimi.themoviedb.ui.main.actors.viewmodel

import android.os.Parcelable
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.models.CombinedCredit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.actorFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setActorDataList( actorList: List<Actor>){
    val update = getCurrentViewStateOrNew()
    update.actorFields.actorList = actorList
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.actorFields.isQueryExhausted = isExhausted
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.actorFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.actorFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setClickedActorId( actorId: Int){
    val update = getCurrentViewStateOrNew()
    update.actorFields.clickedActorId = actorId
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setActorLoadedBeforePassing( actor: Actor? ){
    val update = getCurrentViewStateOrNew()
    update.actorViewFields.actorLoadedBeforePassing = actor
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setActorDetails( actor: Actor){
    val update = getCurrentViewStateOrNew()
    update.actorViewFields.actor = actor
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.clearActorDetails(){
    val update = getCurrentViewStateOrNew()
    update.actorViewFields.actor = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.clearActorCreditsList(){
    val update = getCurrentViewStateOrNew()
    update.actorViewFields.creditsList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ActorViewModel.setActorCreditsList( credits: List<CombinedCredit>){
    val update = getCurrentViewStateOrNew()
    update.actorViewFields.creditsList = credits
    setViewState(update)
}
