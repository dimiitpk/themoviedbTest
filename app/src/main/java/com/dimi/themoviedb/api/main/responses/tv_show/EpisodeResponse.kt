package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.models.TVEpisode
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EpisodeResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("show_id")
    @Expose
    var tvShowId: Int,

    @SerializedName("season_number")
    @Expose
    var seasonNumber: Int,

    @SerializedName("episode_number")
    @Expose
    var episodeNumber: Int,

    @SerializedName("air_date")
    @Expose
    var airDate: String,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("still_path")
    @Expose
    var stillPath: String? = null
) {

    fun toEpisode(): TVEpisode {
        return TVEpisode(
            id = id,
            seasonNumber = seasonNumber,
            overview = overview,
            tvShowId = tvShowId,
            airDate = airDate,
            name = name,
            episodeNumber = episodeNumber,
            stillPath = stillPath,
            tvSeasonId = -1
        )
    }
}