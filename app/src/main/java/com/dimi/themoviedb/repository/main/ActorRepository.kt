package com.dimi.themoviedb.repository.main

import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.ui.main.actors.state.ActorViewState
import com.dimi.themoviedb.utils.DataState
import com.dimi.themoviedb.utils.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope // check ?
interface ActorRepository {

    fun searchActors(
        query: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<ActorViewState>>

    fun getActorDetails(
        actorId: Int,
        stateEvent: StateEvent
    ) : Flow<DataState<ActorViewState>>

    fun getActorCredits(
        actorId: Int,
        stateEvent: StateEvent
    ) : Flow<DataState<ActorViewState>>
}