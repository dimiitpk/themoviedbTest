package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tv_show")
data class TVShow @Ignore constructor(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "number_of_episodes")
    var numberOfEpisodes: Int? = null,

    @ColumnInfo(name = "number_of_seasons")
    var numberOfSeasons: Int? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Double,

    @ColumnInfo(name = "vote_count")
    var voteCount: Int,

    @ColumnInfo(name = "first_air_date")
    var firstAirDate: String? = null,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float,

    @ColumnInfo(name = "overview")
    var overview: String?,

    @ColumnInfo(name = "poster_path")
    var posterPath: String? = null,

    @ColumnInfo(name = "genre_ids")
    var genres: String? = null,

    @Ignore
    var castList: ArrayList<TVCast>? = null,

    @Ignore
    var seasonList: ArrayList<TVSeason>? = null

) : PassingModel(), Parcelable {

    constructor(
        id: Int,
        title: String,
        numberOfEpisodes: Int?,
        numberOfSeasons: Int?,
        popularity: Double,
        voteCount: Int,
        firstAirDate: String?,
        voteAverage: Float,
        overview: String?,
        posterPath: String?,
        genres: String?
    ) : this(
        id, title, numberOfEpisodes, numberOfSeasons, popularity, voteCount, firstAirDate, voteAverage, overview, posterPath, genres, null, null
    )

    override fun toString(): String {
        return "TVShow(id=$id, title='$title', numberOfEpisodes=$numberOfEpisodes, numberOfSeasons=$numberOfSeasons, popularity=$popularity, voteCount=$voteCount, firstAirDate=$firstAirDate, voteAverage=$voteAverage, overview=$overview, posterPath=$posterPath, genres=$genres, castList=$castList, seasonList=$seasonList)"
    }
}