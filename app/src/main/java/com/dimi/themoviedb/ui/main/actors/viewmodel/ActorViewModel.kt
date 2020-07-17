package com.dimi.themoviedb.ui.main.actors.viewmodel

import android.util.Log
import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.repository.main.ActorRepositoryImpl
import com.dimi.themoviedb.repository.main.MovieRepositoryImpl
import com.dimi.themoviedb.ui.BaseViewModel
import com.dimi.themoviedb.ui.main.actors.state.ActorStateEvent
import com.dimi.themoviedb.ui.main.actors.state.ActorStateEvent.*
import com.dimi.themoviedb.ui.main.actors.state.ActorViewState
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
class ActorViewModel
@Inject
constructor(
    private val actorRepository: ActorRepositoryImpl
) : BaseViewModel<ActorViewState>() {

    // take new incoming data and set so can be observe to fragments/activities
    override fun handleNewData(data: ActorViewState) {

        data.actorFields.let { actorFields ->

            actorFields.actorList?.let {
                handleIncomingMovieListData(data)
            }
            actorFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhausted(isQueryExhausted)
            }
        }

        data.actorViewFields.let { actorViewFields ->
            actorViewFields.actor?.let {
                setActorDetails( it )
            }
            actorViewFields.creditsList?.let {
                setActorCreditsList( it )
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if (!isJobAlreadyActive(stateEvent)) {
            val job: Flow<DataState<ActorViewState>> = when (stateEvent) {

                is SearchActors -> {
                    if (stateEvent.clearLayoutManagerState) {
                        clearLayoutManagerState()
                    }
                    actorRepository.searchActors(
                        stateEvent = stateEvent,
                        query = getSearchQuery(),
                        page = getPage()
                    )
                }
                is ActorDetails -> {
                    actorRepository.getActorDetails(
                        actorId = getClickedActorId(),
                        stateEvent = stateEvent
                    )
                }
                is ActorCredits -> {
                    actorRepository.getActorCredits(
                        actorId = getClickedActorId(),
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

    override fun initNewViewState(): ActorViewState {
        return ActorViewState()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}