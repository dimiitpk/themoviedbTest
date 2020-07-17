package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.api.main.responses.VideoListResponse
import com.dimi.themoviedb.models.TVCast
import com.dimi.themoviedb.models.TVSeason
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.utils.ConvertUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVShowDetailsResponse(

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

    @SerializedName("number_of_episodes")
    @Expose
    var numberOfEpisodes: Int,

    @SerializedName("number_of_seasons")
    @Expose
    var numberOfSeasons: Int,

    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("genres") // different key_name in details other than search lists and different type
    @Expose
    var genres: List<GenreResponse>,

    @SerializedName("videos")
    @Expose
    var videosResponse: VideoListResponse,

    @SerializedName("credits")
    @Expose
    var creditsResponse: TVCreditsResponse,

    @SerializedName("seasons")
    @Expose
    var seasonResponse: List<TVSeasonResponse>
) {

    fun toTvShow(): TVShow {

        val array = ArrayList<Int>()
        for( genre in this.genres) {
            array.add(genre.id)
        }
        return TVShow(
            id = id,
            firstAirDate = firstAirDate,
            popularity = popularity,
            genres = ConvertUtils.fromIntArrayToString(array),
            posterPath = posterPath,
            overview = overview,
            voteAverage = voteAverage,
            voteCount = voteCount,
            title = title,
            numberOfEpisodes = numberOfEpisodes,
            numberOfSeasons = numberOfSeasons,
            castList = toCasts(),
            seasonList = toSeasons()
        )
    }

    private fun toCasts(): ArrayList<TVCast> {
        val castList: ArrayList<TVCast> = ArrayList()
        creditsResponse.let {
            for( item in it.cast) {
                val newCast = item.toCast()
                newCast.tvShowId = id
                castList.add(newCast)
            }
        }
        return castList
    }

    private fun toSeasons(): ArrayList<TVSeason> {
        val seasonList: ArrayList<TVSeason> = ArrayList()
        seasonResponse.let { list ->
            for( item in list ) {
                val newSeason = item.toSeason()
                newSeason.tvShowId = id
                seasonList.add(newSeason)
            }
        }
        return seasonList
    }
}