package com.dimi.themoviedb.repository.main

import android.util.Log
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.api.main.OpenApiMainService
import com.dimi.themoviedb.api.main.responses.movie.CastListResponse
import com.dimi.themoviedb.api.main.responses.movie.MovieListSearchResponse
import com.dimi.themoviedb.api.main.responses.VideoListResponse
import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.persistence.MoviesDao
import com.dimi.themoviedb.repository.NetworkBoundResource
import com.dimi.themoviedb.repository.safeApiCall
import com.dimi.themoviedb.session.SessionManager
import com.dimi.themoviedb.ui.main.movies.state.MovieViewState
import com.dimi.themoviedb.ui.main.movies.state.MovieViewState.*
import com.dimi.themoviedb.utils.*
import com.dimi.themoviedb.api.ApiConstants.Companion.API_KEY
import com.dimi.themoviedb.api.main.responses.movie.MovieDetailsResponse
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
class MovieRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val moviesDao: MoviesDao,
    val sessionManager: SessionManager
) : MovieRepository {

    private val TAG: String = "AppDebug"

    override fun searchMovies(
        query: String,
        page: Int,
        genre: Int,
        stateEvent: StateEvent
    ): Flow<DataState<MovieViewState>> {
        return object : NetworkBoundResource<MovieListSearchResponse, List<Movie>, MovieViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                if (genre == GENRE_DEFAULT) {
                    if (query.isBlank())
                        openApiMainService.getPopularMovies(
                            API_KEY,
                            ApiConstants.LANGUAGE_DEFAULT,
                            page
                        )
                    else
                        openApiMainService.searchQuery(
                            API_KEY,
                            ApiConstants.LANGUAGE_DEFAULT,
                            page,
                            ApiConstants.ADULT_DEFAULT,
                            query
                        )
                } else
                    openApiMainService.getMoviesByGenre(
                        API_KEY,
                        ApiConstants.LANGUAGE_DEFAULT,
                        ApiConstants.SORT_BY_DEFAULT,
                        ApiConstants.ADULT_DEFAULT,
                        ApiConstants.VIDEO_DEFAULT,
                        page,
                        genre
                    )
            },
            cacheCall = {
                if (genre == GENRE_DEFAULT)
                    moviesDao.getAllMovies(
                        query = query,
                        page = page
                    )
                else
                    moviesDao.getMoviesByGenre(
                        genre = genre.toString(),
                        page = page
                    )
            }
        ) {
            override suspend fun updateCache(networkObject: MovieListSearchResponse) {

                val movieList = networkObject.toMovieList()
                withContext(IO) {
                    for (movie in movieList) {
                        try {
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting movie: $movie")
                                // if already exist in base movie with same primary key, onConflict will be triggered
                                // bcs onConflictStrategy is IGNORE if there is one, return -1
                                // then we need to update and in that way we will not lose foreign keys with movie_cast
                                // bcs onCascade = DELETE will not be triggered by IGNORE CONFLICT
                                moviesDao.insert(movie).let { returnValue ->
                                    if (returnValue == -1L)
                                        moviesDao.update(movie)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on movie with title: ${movie.title}. " +
                                        "${e.message}"
                            )
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<Movie>): DataState<MovieViewState> {
                val viewState = MovieViewState(
                    movieFields = MovieFields(
                        movieList = resultObj
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


    override fun getMovieVideos(
        movieId: Int,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.getVideosByMovieId(
                movieId,
                API_KEY,
                ApiConstants.LANGUAGE_DEFAULT
            )
        }
        emit(
            object : ApiResponseHandler<MovieViewState, VideoListResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: VideoListResponse): DataState<MovieViewState> {

                    Log.d("MRKNASMOGA", resultObj.toYoutubeTrailersList().toString())
                    val viewState = MovieViewState(
                        viewMovieFields = ViewMovieFields(
                            videoList = resultObj.toYoutubeTrailersList()
                        )
                    )
                    return DataState.data(
                        response = null,
                        data = viewState,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

    override fun getMovieDetails(
        movieId: Int,
        stateEvent: StateEvent
    ): Flow<DataState<MovieViewState>> {
        return object : NetworkBoundResource<MovieDetailsResponse, Movie, MovieViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                openApiMainService.getMovieDetails(
                    movieId = movieId,
                    apiKey = API_KEY,
                    language = ApiConstants.LANGUAGE_DEFAULT,
                    appendTo = "credits"
                )
            },
            cacheCall = {
                moviesDao.getMovie(
                    movieId = movieId
                )
            }
        ) {
            override suspend fun updateCache(networkObject: MovieDetailsResponse) {
                val movie = networkObject.toMovie()

                Log.d(TAG, "updateLocalDb: inserting movie: $movie")
                moviesDao.insert(movie).let { returnValue ->
                    if (returnValue == -1L)
                        moviesDao.update(movie)
                }
                withContext(IO) {

                    movie.castList.let { castList ->
                        if (castList.isNotEmpty()) {
                            for (cast in castList) {
                                try {
                                    launch {
                                        Log.d(TAG, "updateLocalDb: inserting cast: $cast")
                                        moviesDao.insert(cast)
                                    }

                                } catch (e: Exception) {
                                    Log.e(
                                        TAG,
                                        "updateLocalDb: error updating cache data on movie with title: ${movie.title}. " +
                                                "${e.message}"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: Movie): DataState<MovieViewState> {
                val viewState = MovieViewState(
                    viewMovieFields = ViewMovieFields(
                        movie = resultObj,
                        castList = resultObj.castList
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

            override suspend fun updateCacheBeforeShow(resultObj: Movie) {

                resultObj.castList.addAll(moviesDao.getMovieCast(movieId = resultObj.id))
            }

        }.result
    }

}