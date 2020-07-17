package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "tv_season",
    foreignKeys = [
        ForeignKey(
            entity = TVShow::class,
            parentColumns = ["id"],
            childColumns = ["tvShowId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["tvShowId", "air_date", "poster_path"],
            unique = true
        )
    ]
)
data class TVSeason(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "air_date")
    var airDate: String? = null,

    @ColumnInfo(name = "episode_count")
    var episodeCount: Int,

    @ColumnInfo(name = "name")
    var seasonName: String,

    @ColumnInfo(name = "overview")
    var overview: String? = null,

    @ColumnInfo(name = "poster_path")
    var posterPath: String? = null,

    @ColumnInfo(name = "season_number")
    var seasonNumber: Int,

    @ColumnInfo(name = "tvShowId")
    var tvShowId: Int

) : Parcelable {

    override fun toString(): String {
        return "TVSeason(id=$id, airDate='$airDate', episodeCount=$episodeCount, seasonName='$seasonName', overview='$overview', posterPath=$posterPath, seasonNumber=$seasonNumber, tvShowId=$tvShowId)"
    }
}