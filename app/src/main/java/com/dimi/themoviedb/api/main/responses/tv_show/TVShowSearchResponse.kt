package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.utils.ConvertUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVShowSearchResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var title: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Double,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,

    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String? = null,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("genre_ids")
    @Expose
    var genres: ArrayList<Int>? = null

) {

    fun toTVShow(): TVShow {
        return TVShow(
            id = id,
            title = title,
            popularity = popularity,
            voteCount = voteCount,
            firstAirDate = firstAirDate,
            voteAverage = voteAverage,
            overview = overview,
            posterPath = posterPath,
            genres = ConvertUtils.fromIntArrayToString(genres!!),
            numberOfSeasons = 0,
            numberOfEpisodes = 0,
            castList = null
        )
    }

}