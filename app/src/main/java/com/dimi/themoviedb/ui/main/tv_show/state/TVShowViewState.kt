package com.dimi.themoviedb.ui.main.tv_show.state

import android.os.Parcelable
import com.dimi.themoviedb.models.*
import kotlinx.android.parcel.Parcelize

const val TV_SHOW_VIEW_STATE_BUNDLE_KEY = "com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState"
@Parcelize
data class TVShowViewState(
    var tvShowFields: TVShowFields = TVShowFields(),

    var viewTvShowFields: ViewTVShowFields = ViewTVShowFields(),

    var viewSeasonFields: ViewSeasonFields = ViewSeasonFields(),

    var viewEpisodeFields: ViewEpisodeFields = ViewEpisodeFields()
) : Parcelable
{
    @Parcelize
    data class TVShowFields(
        var tvShowList: List<TVShow>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var layoutManagerState: Parcelable? = null,
        var searchGenre: Int? = null,
        var selectedTab: Int? = null,
        var clickedTVShowId: Int? = null
    ) : Parcelable

    @Parcelize
    data class ViewTVShowFields(
        var tvShowLoadedBeforePassing: TVShow? = null,
        var tvShow: TVShow? = null,
        var castList: List<TVCast>? = null,
        var videoList: List<Video>? = null,
        var seasonList: List<TVSeason>? = null
    ): Parcelable

    @Parcelize
    data class ViewSeasonFields(
        var tvSeasonLoadedBeforePassing: TVSeason? = null,
        var tvSeason: TVSeason? = null,
        var episodeList: List<TVEpisode>? = null
    ): Parcelable

    @Parcelize
    data class ViewEpisodeFields(
        var tvEpisodeLoadedBeforePassing: TVEpisode? = null,
        var tvEpisode: TVEpisode? = null
    ): Parcelable
}