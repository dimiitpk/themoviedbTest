package com.dimi.themoviedb.api.main

import com.dimi.themoviedb.api.main.responses.actor.ActorListSearchResponse
import com.dimi.themoviedb.api.main.responses.actor.ActorSearchResponse
import com.dimi.themoviedb.api.main.responses.movie.CastListResponse
import com.dimi.themoviedb.api.main.responses.movie.MovieListSearchResponse
import com.dimi.themoviedb.api.main.responses.VideoListResponse
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.api.main.responses.actor.ActorKnownForResponse
import com.dimi.themoviedb.api.main.responses.movie.MovieDetailsResponse
import com.dimi.themoviedb.api.main.responses.tv_show.TVSeasonResponse
import com.dimi.themoviedb.api.main.responses.tv_show.TVShowDetailsResponse
import com.dimi.themoviedb.api.main.responses.tv_show.TVShowListSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenApiMainService {

    @GET(ApiConstants.POPULAR_MOVIES_ENDPOINT)
    suspend fun getPopularMovies(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListSearchResponse

    @GET(ApiConstants.MOVIES_SEARCH_ENDPOINT)
    suspend fun searchQuery(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(ApiConstants.ADULT_REQUEST_PARAM) include_adult: Boolean,
        @Query(ApiConstants.QUERY_REQUEST_PARAM) query: String
    ): MovieListSearchResponse

    @GET(ApiConstants.MOVIES_GENRE_ENDPOINT)
    suspend fun getMoviesByGenre(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.SORT_BY_REQUEST_PARAM) sortBy: String,
        @Query(ApiConstants.ADULT_REQUEST_PARAM) includeAdult: Boolean,
        @Query(ApiConstants.VIDEO_REQUEST_PARAM) includeVideo: Boolean,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(ApiConstants.GENRE_REQUEST_PARAM) genre: Int
    ): MovieListSearchResponse

    @GET("movie/{movieId}/videos")
    suspend fun getVideosByMovieId(
        @Path("movieId") movieId: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String
    ) : VideoListResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.APPEND_TO_RESPONSE_PARAM) appendTo: String
    ): MovieDetailsResponse

    @GET(ApiConstants.ACTORS_SEARCH_ENDPOINT)
    suspend fun actorSearchQuery(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(ApiConstants.ADULT_REQUEST_PARAM) include_adult: Boolean,
        @Query(ApiConstants.QUERY_REQUEST_PARAM) query: String
    ): ActorListSearchResponse

    @GET( ApiConstants.POPULAR_ACTORS_ENDPOINT )
    suspend fun getPopularActors(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int
    ): ActorListSearchResponse

    @GET("person/{personId}")
    suspend fun getActorDetails(
        @Path("personId") personId: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String
    ): ActorSearchResponse


    @GET(ApiConstants.POPULAR_TV_SHOW_ENDPOINT)
    suspend fun getPopularTvShows(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int
    ): TVShowListSearchResponse

    @GET(ApiConstants.TV_SHOW_SEARCH_ENDPOINT)
    suspend fun searchTvShowQuery(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(ApiConstants.ADULT_REQUEST_PARAM) include_adult: Boolean,
        @Query(ApiConstants.QUERY_REQUEST_PARAM) query: String
    ): TVShowListSearchResponse

    @GET(ApiConstants.TV_SHOW_GENRE_ENDPOINT)
    suspend fun getTvShowsByGenre(
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.SORT_BY_REQUEST_PARAM) sortBy: String,
        @Query(ApiConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(ApiConstants.TIMEZONE_REQUEST_PARAM) timeZone: String,
        @Query(ApiConstants.GENRE_REQUEST_PARAM) genre: Int,
        @Query(ApiConstants.FIRST_AIR_DATES_REQUEST_PARAM) firstAirDates: Boolean
    ): TVShowListSearchResponse

    @GET("tv/{tvShowId}")
    suspend fun getTVShowDetails(
        @Path("tvShowId") tvShowId: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(ApiConstants.APPEND_TO_RESPONSE_PARAM) appendTo: String
    ): TVShowDetailsResponse

    @GET("tv/{tvShowId}/videos")
    suspend fun getVideosByTvShowId(
        @Path("tvShowId") tvShowId: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String
    ) : VideoListResponse

    @GET("tv/{tvShowId}/season/{seasonNumber}")
    suspend fun getTvSeasonEpisodes(
        @Path("tvShowId") tvShowId: Int,
        @Path("seasonNumber") seasonNumber: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String
    ) : TVSeasonResponse

    @GET("person/{personId}/combined_credits")
    suspend fun getActorKnownFor(
        @Path("personId") personId: Int,
        @Query(ApiConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(ApiConstants.LANGUAGE_REQUEST_PARAM) language: String
    ): ActorKnownForResponse
}