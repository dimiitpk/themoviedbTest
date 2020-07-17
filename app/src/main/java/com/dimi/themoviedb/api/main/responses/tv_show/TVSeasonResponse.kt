package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.models.TVEpisode
import com.dimi.themoviedb.models.TVSeason
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVSeasonResponse(

    @SerializedName("air_date")
    @Expose
    var airDate: String,

    @SerializedName("episode_count")
    @Expose
    var episodeCount: Int,

    @SerializedName("name")
    @Expose
    var seasonName: String,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("season_number")
    @Expose
    var seasonNumber: Int,

    @SerializedName("episodes") // if u look for season id episodes
    @Expose
    var episodes: List<EpisodeResponse>
) {

    fun toSeason(): TVSeason {
        return TVSeason(
            airDate = airDate,
            tvShowId = -1,
            overview = overview,
            posterPath = posterPath,
            episodeCount = episodeCount,
            seasonName = seasonName,
            seasonNumber = seasonNumber
        )
    }

    fun toEpisodes( tvSeasonId: Long ): ArrayList<TVEpisode> {
        val episodeList: ArrayList<TVEpisode> = ArrayList()
        episodes.let { list ->
            for( item in list ) {
                val episode = item.toEpisode()
                episode.tvSeasonId = tvSeasonId
                episodeList.add( episode )
            }
        }
        return episodeList
    }

    override fun toString(): String {
        return "TVSeasonResponse(airDate='$airDate', episodeCount=$episodeCount, seasonName='$seasonName', overview='$overview', posterPath=$posterPath, seasonNumber=$seasonNumber, episodes=$episodes)"
    }


}