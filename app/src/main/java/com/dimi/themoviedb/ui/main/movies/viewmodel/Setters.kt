package com.dimi.themoviedb.ui.main.movies.viewmodel

import android.os.Parcelable
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.models.Video
import com.dimi.themoviedb.ui.main.movies.state.MovieStateEvent
import com.dimi.themoviedb.utils.Constants.Companion.GENRE_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.movieFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setMovieListData( movieList: List<Movie>){
    val update = getCurrentViewStateOrNew()
    update.movieFields.movieList = movieList
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setMovieLoadedBeforePassing( movie: Movie? ){
    val update = getCurrentViewStateOrNew()
    update.viewMovieFields.movieLoadedBeforePassing = movie
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setMovieCastList(list: List<Cast> ){
    val update = getCurrentViewStateOrNew()
    update.viewMovieFields.castList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.clearMovieCastList() {
    val update = getCurrentViewStateOrNew()
    update.viewMovieFields.castList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setMovieVideoList( list: List<Video> ){
    val update = getCurrentViewStateOrNew()
    update.viewMovieFields.videoList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.clearMovieVideoList() {
    val update = getCurrentViewStateOrNew()
    MovieStateEvent.GetMovieVideos.isEventCalled = false
    update.viewMovieFields.videoList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setViewMovie( movie: Movie ) {
    val update = getCurrentViewStateOrNew()
    update.viewMovieFields.movie = movie
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.clearViewMovie( ) {
    val update = getCurrentViewStateOrNew()
    update.viewMovieFields.movie = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setClickedMovieId( movieId: Int) {
    val update = getCurrentViewStateOrNew()
    update.movieFields.clickedMovieId = movieId
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.movieFields.isQueryExhausted = isExhausted
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.movieFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.movieFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setSelectedTab( position: Int ) {
    val update = getCurrentViewStateOrNew()
    update.movieFields.selectedTab = position
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.setGenre( genreId: Int) {
    val update = getCurrentViewStateOrNew()
    update.movieFields.searchGenre = genreId
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MovieViewModel.clearGenre() {
    val update = getCurrentViewStateOrNew()
    update.movieFields.searchGenre = GENRE_DEFAULT
    setViewState(update)
}