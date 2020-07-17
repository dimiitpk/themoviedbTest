package com.dimi.themoviedb.ui.main.tv_show.viewmodel

import android.os.Parcelable
import com.dimi.themoviedb.models.*
import com.dimi.themoviedb.utils.Constants.Companion.GENRE_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().tvShowFields.searchQuery
        ?: ""
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getIsQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().tvShowFields.isQueryExhausted
        ?: false
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().tvShowFields.page
        ?: 1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getGenre(): Int {
    return getCurrentViewStateOrNew().tvShowFields.searchGenre
        ?: GENRE_DEFAULT
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getLayoutManagerState(): Parcelable? {
    getCurrentViewStateOrNew().let {
        return it.tvShowFields.layoutManagerState
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTVShowLoadedBeforePassing(): TVShow? {
    return getCurrentViewStateOrNew().viewTvShowFields.tvShowLoadedBeforePassing
        ?: return null
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTVSeasonLoadedBeforePassing(): TVSeason? {
    return getCurrentViewStateOrNew().viewSeasonFields.tvSeasonLoadedBeforePassing
        ?: return null
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTVEpisodeLoadedBeforePassing(): TVEpisode? {
    return getCurrentViewStateOrNew().viewEpisodeFields.tvEpisodeLoadedBeforePassing
        ?: return null
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getClickedTvShowId(): Int {
    return getCurrentViewStateOrNew().tvShowFields.clickedTVShowId
        ?: return -1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getSelectedTab(): Int {
    return getCurrentViewStateOrNew().tvShowFields.selectedTab
        ?: return 0
}
@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTvShowCastList(): List<TVCast> {
    return getCurrentViewStateOrNew().viewTvShowFields.castList
        ?: ArrayList()
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTvShowVideoList(): List<Video> {
    return getCurrentViewStateOrNew().viewTvShowFields.videoList
        ?: ArrayList()
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTvShowId(): Int {
    return getCurrentViewStateOrNew().tvShowFields.clickedTVShowId
        ?: -1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTVEpisode(): TVEpisode? {
    return getCurrentViewStateOrNew().viewEpisodeFields.tvEpisode
}

@FlowPreview
@ExperimentalCoroutinesApi
fun TVShowViewModel.getTvSeason(): TVSeason {
    return getCurrentViewStateOrNew().viewSeasonFields.tvSeason
        ?: TVSeason( -1, "", 1, "", "", "", 1, -1)
}

@FlowPreview
    @ExperimentalCoroutinesApi
    fun TVShowViewModel.getTvShow(): TVShow? {
        return getCurrentViewStateOrNew().viewTvShowFields.tvShow
}
