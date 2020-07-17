package com.dimi.themoviedb.repository.main

import android.util.Log
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.api.main.OpenApiMainService
import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.repository.NetworkBoundResource
import com.dimi.themoviedb.session.SessionManager
import com.dimi.themoviedb.utils.*
import com.dimi.themoviedb.api.ApiConstants.Companion.API_KEY
import com.dimi.themoviedb.api.ApiConstants.Companion.LANGUAGE_DEFAULT
import com.dimi.themoviedb.api.main.responses.VideoListResponse
import com.dimi.themoviedb.api.main.responses.tv_show.TVSeasonResponse
import com.dimi.themoviedb.api.main.responses.tv_show.TVShowDetailsResponse
import com.dimi.themoviedb.api.main.responses.tv_show.TVShowListSearchResponse
import com.dimi.themoviedb.models.TVEpisode
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.persistence.TvShowsDao
import com.dimi.themoviedb.repository.safeApiCall
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState.*
import com.dimi.themoviedb.utils.Constants.Companion.GENRE_DEFAULT
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
@MainScope // check?
class TVShowRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val tvShowsDao: TvShowsDao,
    val sessionManager: SessionManager
) : TVShowRepository {

    private val TAG: String = "AppDebug"

    override fun searchTVShows(
        query: String,
        page: Int,
        genre: Int,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>> {
        return object :
            NetworkBoundResource<TVShowListSearchResponse, List<TVShow>, TVShowViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    if (genre == GENRE_DEFAULT) {
                        if (query.isBlank())
                            openApiMainService.getPopularTvShows(
                                API_KEY,
                                LANGUAGE_DEFAULT,
                                page
                            )
                        else
                            openApiMainService.searchTvShowQuery(
                                API_KEY,
                                LANGUAGE_DEFAULT,
                                page,
                                ApiConstants.ADULT_DEFAULT,
                                query
                            )
                    } else
                        openApiMainService.getTvShowsByGenre(
                            API_KEY,
                            LANGUAGE_DEFAULT,
                            ApiConstants.SORT_BY_DEFAULT,
                            page,
                            ApiConstants.TIMEZONE_DEFAULT,
                            genre,
                            ApiConstants.FIRST_AIR_DEFAULT
                        )
                },
                cacheCall = {
                    if (genre == GENRE_DEFAULT)
                        tvShowsDao.getAllTvShows(
                            query = query,
                            page = page
                        )
                    else
                        tvShowsDao.getTVShowsByGenre(
                            genre = genre.toString(),
                            page = page
                        )
                }
            ) {
            override suspend fun updateCache(networkObject: TVShowListSearchResponse) {

                val tvShowList = networkObject.toTVShowList()
                withContext(IO) {
                    for (tvShow in tvShowList) {
                        try {
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting tvShow: $tvShow")

                                tvShowsDao.insert(tvShow).let { returnValue ->
                                    if (returnValue == -1L)
                                        tvShowsDao.update(tvShow)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on tvShow with title: ${tvShow.title}. " +
                                        "${e.message}"
                            )
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<TVShow>): DataState<TVShowViewState> {
                val viewState = TVShowViewState(
                    tvShowFields = TVShowFields(
                        tvShowList = resultObj
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

    override fun getTvShowVideos(
        tvShowId: Int,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.getVideosByTvShowId(
                tvShowId,
                API_KEY,
                LANGUAGE_DEFAULT
            )
        }
        emit(
            object : ApiResponseHandler<TVShowViewState, VideoListResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: VideoListResponse): DataState<TVShowViewState> {

                    return DataState.data(
                        response = null,
                        data = TVShowViewState(
                            viewTvShowFields = ViewTVShowFields(
                                videoList = resultObj.toYoutubeTrailersList()
                            )
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

    override fun getTvSeasonEpisodes(
        tvShowId: Int,
        seasonNumber: Int,
        tvSeasonId: Long,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>> {
        return object : NetworkBoundResource<TVSeasonResponse, List<TVEpisode>, TVShowViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                openApiMainService.getTvSeasonEpisodes(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    apiKey = API_KEY,
                    language = LANGUAGE_DEFAULT
                )
            },
            cacheCall = {
                tvShowsDao.getTvShowSeasonEpisodes(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber
                )
            }
        ) {
            override suspend fun updateCache(networkObject: TVSeasonResponse) {
                withContext(IO) {
                    for (episode in networkObject.toEpisodes( tvSeasonId )) {
                        try {
                            launch {

                                tvShowsDao.insert(episode)
                                Log.d(TAG, "updateLocalDb: inserting tv episode: $episode")
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on episode with title: ${episode.name}. " +
                                        "${e.message}"
                            )
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<TVEpisode>): DataState<TVShowViewState> {
                val viewState = TVShowViewState(
                    viewSeasonFields = ViewSeasonFields(
                        episodeList = resultObj
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

    override fun getTvShowDetails(
        tvShowId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<TVShowViewState>> {
        return object : NetworkBoundResource<TVShowDetailsResponse, TVShow, TVShowViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                openApiMainService.getTVShowDetails(
                    tvShowId = tvShowId,
                    apiKey = API_KEY,
                    language = LANGUAGE_DEFAULT,
                    appendTo = "videos,credits"
                )
            },
            cacheCall = {
                tvShowsDao.getTvShow(
                    tvShowId = tvShowId
                )
            }
        ) {
            override suspend fun updateCache(networkObject: TVShowDetailsResponse) {

                val tvShow = networkObject.toTvShow()
                withContext(IO) {
                    try {
                        launch {

                            Log.d(TAG, "updateLocalDb: inserting tvShow: $tvShow")
                            tvShowsDao.insert(tvShow).let { returnValue ->
                                if (returnValue == -1L)
                                    tvShowsDao.update(tvShow)
                            }

                            tvShow.castList?.let { castList ->
                                for (cast in castList) {

                                    Log.d(TAG, "updateLocalDb: inserting cast: $cast")
                                    tvShowsDao.insert(cast)
                                }
                            }

                            tvShow.seasonList?.let { seasons ->
                                for (season in seasons) {

                                    tvShowsDao.loadInsertOrUpdate( season )
                                    Log.d(TAG, "updateLocalDb2: inserting tv season: $season")
                                }
                            }

                        }
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "updateLocalDb: error updating cache data on tvShow with title: ${tvShow.title}. " +
                                    "${e.message}"
                        )
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: TVShow): DataState<TVShowViewState> {
                val viewState = TVShowViewState(
                    viewTvShowFields = ViewTVShowFields(
                        tvShow = resultObj,
                        castList = resultObj.castList,
                        seasonList = resultObj.seasonList
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

            override suspend fun updateCacheBeforeShow(resultObj: TVShow) {
                withContext(IO) {
                    try {
                        launch {
                            if (resultObj.castList == null) {
                                resultObj.castList = ArrayList()
                                resultObj.castList!!.addAll(tvShowsDao.getTvShowCast(tvShowId = resultObj.id))
                            }

                            if (resultObj.seasonList == null) {
                                resultObj.seasonList = ArrayList()
                                resultObj.seasonList!!.addAll(tvShowsDao.getTvShowSeasons(tvShowId = resultObj.id))
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "updateLocalDb: error updating cache data on tvShow with title: ${resultObj.title}. " +
                                    "${e.message}"
                        )
                    }
                }
            }

        }.result
    }
}