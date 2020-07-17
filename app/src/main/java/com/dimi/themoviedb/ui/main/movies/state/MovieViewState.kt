package com.dimi.themoviedb.ui.main.movies.state

import android.os.Parcelable
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.models.Video
import kotlinx.android.parcel.Parcelize

const val MOVIE_VIEW_STATE_BUNDLE_KEY = "com.dimi.themoviedb.ui.main.movies.state.MovieViewState"

@Parcelize
data class MovieViewState (

    var movieFields: MovieFields = MovieFields(),

    var viewMovieFields: ViewMovieFields = ViewMovieFields()

): Parcelable {

    @Parcelize
    data class MovieFields(
        var movieList: List<Movie>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var layoutManagerState: Parcelable? = null,
        var searchGenre: Int? = null,
        var selectedTab: Int? = null,
        var clickedMovieId: Int? = null
    ) : Parcelable

    @Parcelize
    data class ViewMovieFields(
        var movieLoadedBeforePassing: Movie? = null,
        var movie: Movie? = null,
        var videoList: List<Video>? = null,
        var castList: List<Cast>? = null
    ) : Parcelable
}