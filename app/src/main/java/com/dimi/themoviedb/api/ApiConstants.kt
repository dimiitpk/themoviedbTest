package com.dimi.themoviedb.api

class ApiConstants {

    companion object{

        // &append_to_response=videos
        const val BASE_URL = "https://api.themoviedb.org/3/"

        const val POPULAR_MOVIES_ENDPOINT = "movie/popular"
        const val POPULAR_ACTORS_ENDPOINT = "person/popular"
        const val POPULAR_TV_SHOW_ENDPOINT = "tv/popular"

        const val MOVIES_SEARCH_ENDPOINT = "search/movie"
        const val MOVIES_GENRE_ENDPOINT = "discover/movie"
        const val TV_SHOW_SEARCH_ENDPOINT = "search/tv"
        const val TV_SHOW_GENRE_ENDPOINT = "discover/tv"
        const val ACTORS_SEARCH_ENDPOINT = "search/person"


        private const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/"
        const val SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300"
        const val BIG_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w500"

        const val API_KEY = "5e17c647dba4763165126877498f1467"

        const val API_KEY_REQUEST_PARAM = "api_key"
        const val LANGUAGE_REQUEST_PARAM = "language"
        const val PAGE_REQUEST_PARAM = "page"
        const val SORT_BY_REQUEST_PARAM = "sort_by"
        const val ADULT_REQUEST_PARAM = "include_adult"
        const val FIRST_AIR_DATES_REQUEST_PARAM = "include_null_first_air_dates"
        const val VIDEO_REQUEST_PARAM = "include_video"
        const val QUERY_REQUEST_PARAM = "query"
        const val TIMEZONE_REQUEST_PARAM = "timezone"
        const val GENRE_REQUEST_PARAM = "with_genres"
        const val APPEND_TO_RESPONSE_PARAM = "append_to_response"

        const val PAGE_SIZE = 20

        const val TIMEZONE_DEFAULT = "America%2FNew_York"
        const val LANGUAGE_DEFAULT = "en-US"
        const val ADULT_DEFAULT = false
        const val VIDEO_DEFAULT = false
        const val FIRST_AIR_DEFAULT = false
        const val SORT_BY_DEFAULT = "popularity.desc"
        const val VIDEOS_AND_CREDITS_APPEND = "videos%2Ccredits"


        // tv show details with cast and videos
        // https://api.themoviedb.org/3/tv/60735?api_key=5e17c647dba4763165126877498f1467&language=en-US&append_to_response=videos%2Ccredits

        // cast for tv show
        // https://api.themoviedb.org/3/tv/60735/credits?api_key=5e17c647dba4763165126877498f1467&language=en-US

        // SEASON EPISODES by season id WITH TRAILERS
        //https://api.themoviedb.org/3/tv/1399/season/6?api_key=5e17c647dba4763165126877498f1467&language=en-US&append_to_response=videos
    }
}