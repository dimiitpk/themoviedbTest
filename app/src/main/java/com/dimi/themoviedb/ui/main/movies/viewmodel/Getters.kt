package com.dimi.themoviedb.ui.main.movies.viewmodel

import android.os.Parcelable
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.models.Video
import com.dimi.themoviedb.utils.Constants.Companion.GENRE_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().movieFields.searchQuery
        ?: ""
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getIsQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().movieFields.isQueryExhausted
        ?: false
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getMovieLoadedBeforePassing(): Movie? {
    return getCurrentViewStateOrNew().viewMovieFields.movieLoadedBeforePassing
        ?: return null
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().movieFields.page
        ?: 1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getMovieCastList(): List<Cast> {
    return getCurrentViewStateOrNew().viewMovieFields.castList
        ?: ArrayList()
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getGenre(): Int {
    return getCurrentViewStateOrNew().movieFields.searchGenre
        ?: GENRE_DEFAULT
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getMovieVideoList(): List<Video> {
    return getCurrentViewStateOrNew().viewMovieFields.videoList
        ?: ArrayList()
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getViewMovie(): Movie {
    return getCurrentViewStateOrNew().viewMovieFields.movie
        ?: Movie( -1, "", 0.0, 0, "", 0.0f, "", "", "")
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getClickedMovieId(): Int {
    return getCurrentViewStateOrNew().movieFields.clickedMovieId
        ?: return -1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getMovieId(): Int {
    return getCurrentViewStateOrNew().viewMovieFields.movie?.id
        ?: return -1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getLayoutManagerState(): Parcelable? {
    getCurrentViewStateOrNew().let {
        return it.movieFields.layoutManagerState
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.getSelectedTab(): Int {
    return getCurrentViewStateOrNew().movieFields.selectedTab
        ?: return 0
}