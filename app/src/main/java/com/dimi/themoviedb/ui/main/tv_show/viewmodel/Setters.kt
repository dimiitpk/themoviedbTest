package com.dimi.themoviedb.ui.main.tv_show.viewmodel

import android.os.Parcelable
import com.dimi.themoviedb.models.*
import com.dimi.themoviedb.utils.Constants.Companion.GENRE_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTVShowListData( tvShowList: List<TVShow>){
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.tvShowList = tvShowList
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTvShowLoadedBeforePassing( tvShow: TVShow? ){
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.tvShowLoadedBeforePassing = tvShow
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTVSeasonLoadedBeforePassing( tvSeason: TVSeason? ){
    val update = getCurrentViewStateOrNew()
    update.viewSeasonFields.tvSeasonLoadedBeforePassing = tvSeason
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTvEpisodeLoadedBeforePassing( tvEpisode: TVEpisode? ){
    val update = getCurrentViewStateOrNew()
    update.viewEpisodeFields.tvEpisodeLoadedBeforePassing = tvEpisode
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.isQueryExhausted = isExhausted
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setSelectedTab( position: Int ) {
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.selectedTab = position
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setGenre( genreId: Int) {
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.searchGenre = genreId
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearGenre() {
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.searchGenre = GENRE_DEFAULT
    setViewState(update)
}


@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTvShowCastList(list: List<TVCast> ){
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.castList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearTvShowVideoList() {
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.videoList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearTvShowCastList() {
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.castList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTvShowVideoList( list: List<Video> ){
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.videoList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearTvShowEpisodeList() {
    val update = getCurrentViewStateOrNew()
    update.viewSeasonFields.episodeList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTVSeasonEpisodeList( list: List<TVEpisode> ){
    val update = getCurrentViewStateOrNew()
    update.viewSeasonFields.episodeList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTVSeasonList( list: List<TVSeason> ){
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.seasonList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearTvShowSeasonList() {
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.seasonList = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setClickedTvShowId( tvShowId: Int) {
    val update = getCurrentViewStateOrNew()
    update.tvShowFields.clickedTVShowId = tvShowId
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTvSeason( tvSeason: TVSeason) {
    val update = getCurrentViewStateOrNew()
    update.viewSeasonFields.tvSeason = tvSeason
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setEpisode( episode: TVEpisode) {
    val update = getCurrentViewStateOrNew()
    update.viewEpisodeFields.tvEpisode = episode
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearEpisode() {
    val update = getCurrentViewStateOrNew()
    update.viewEpisodeFields.tvEpisode = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.setTvShow( tvShow: TVShow ) {
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.tvShow = tvShow
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.clearTvShow( ) {
    val update = getCurrentViewStateOrNew()
    update.viewTvShowFields.tvShow = null
    setViewState(update)
}