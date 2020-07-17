package com.dimi.themoviedb.repository.main

import android.util.Log
import com.dimi.themoviedb.api.main.OpenApiMainService
import com.dimi.themoviedb.api.main.responses.actor.ActorListSearchResponse
import com.dimi.themoviedb.api.main.responses.actor.ActorSearchResponse
import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.persistence.ActorsDao
import com.dimi.themoviedb.repository.NetworkBoundResource
import com.dimi.themoviedb.session.SessionManager
import com.dimi.themoviedb.ui.main.actors.state.ActorViewState
import com.dimi.themoviedb.ui.main.actors.state.ActorViewState.*
import com.dimi.themoviedb.api.ApiConstants.Companion.ADULT_DEFAULT
import com.dimi.themoviedb.api.ApiConstants.Companion.API_KEY
import com.dimi.themoviedb.api.ApiConstants.Companion.LANGUAGE_DEFAULT
import com.dimi.themoviedb.api.main.responses.actor.ActorKnownForResponse
import com.dimi.themoviedb.models.CombinedCredit
import com.dimi.themoviedb.utils.DataState
import com.dimi.themoviedb.utils.StateEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
@MainScope // check?
class ActorRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val actorsDao: ActorsDao,
    val sessionManager: SessionManager
) : ActorRepository {


    private val TAG: String = "AppDebug"

    override fun searchActors(
        query: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<ActorViewState>> {

        return object : NetworkBoundResource<ActorListSearchResponse, List<Actor>, ActorViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                if (query.isBlank())
                    openApiMainService.getPopularActors(
                        API_KEY,
                        LANGUAGE_DEFAULT,
                        page
                    )
                else
                    openApiMainService.actorSearchQuery(
                        API_KEY,
                        LANGUAGE_DEFAULT,
                        page,
                        ADULT_DEFAULT,
                        query
                    )
            },
            cacheCall = {
                actorsDao.getAllActors(
                    query = query,
                    page = page
                )
            }
        ) {
            override fun handleCacheSuccess(resultObj: List<Actor>): DataState<ActorViewState> {
                val viewState = ActorViewState(
                    actorFields = ActorFields(
                        actorList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

            override suspend fun updateCache(networkObject: ActorListSearchResponse) {
                val actorList = networkObject.toActorsList()
                withContext(IO) {
                    for (actor in actorList) {
                        if (actor.department == "Acting") {
                            try {
                                launch {
                                    Log.d(TAG, "updateLocalDb: inserting actor: $actor")

                                    /*
                                    *  if actor is not inserted in db, it will be,
                                    *  otherwise insert request will be ignored
                                    * */
                                    actorsDao.insert(actor)
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "updateLocalDb: error updating cache data on actor with name: ${actor.name}. " +
                                            "${e.message}"
                                )
                            }
                        }
                    }
                }
            }
        }.result
    }

    override fun getActorDetails(
        actorId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<ActorViewState>> {
        return object : NetworkBoundResource<ActorSearchResponse, Actor, ActorViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                openApiMainService.getActorDetails(
                    actorId,
                    API_KEY,
                    LANGUAGE_DEFAULT
                )
            },
            cacheCall = {
                actorsDao.getActor(actorId)
            }
        ) {
            override suspend fun updateCache(networkObject: ActorSearchResponse) {
                val actor = networkObject.toActor()

                Log.d(TAG, "updateLocalDb: inserting actor: $actor")
                /*
                *  if actor is not inserted in db, it will be,
                *  otherwise actor will be updated
                *  with this logic actor will not lost credits ( foreign keys)
                * */
                actorsDao.insert(actor).let { returnValue ->
                    if (returnValue == -1L)
                        actorsDao.update(actor)
                }
            }

            override fun handleCacheSuccess(resultObj: Actor): DataState<ActorViewState> {

                val viewState = ActorViewState(
                    actorViewFields = ActorViewFields(
                        actor = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }

    override fun getActorCredits(
        actorId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<ActorViewState>> {
        return object :
            NetworkBoundResource<ActorKnownForResponse, List<CombinedCredit>, ActorViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService.getActorKnownFor(
                        actorId,
                        API_KEY,
                        LANGUAGE_DEFAULT
                    )
                },
                cacheCall = {
                    actorsDao.getActorCredits(actorId)
                }
            ) {
            override suspend fun updateCache(networkObject: ActorKnownForResponse) {
                val creditList = networkObject.toCombinedCredits()
                withContext(IO) {
                    for (credit in creditList) {
                        try {
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting combined credit: $credit")
                                actorsDao.getActor(credit.actorId)?.let {
                                    actorsDao.insert(credit)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on combined credit with actor id : ${credit.actorId}. " +
                                        "${e.message}"
                            )
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CombinedCredit>): DataState<ActorViewState> {
                val viewState = ActorViewState(
                    actorViewFields = ActorViewFields(
                        creditsList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }
}