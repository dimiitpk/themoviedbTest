package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "tv_episode",
    foreignKeys = [
        ForeignKey(
            entity = TVSeason::class,
            parentColumns = ["id"],
            childColumns = ["season_id"],
            onDelete = CASCADE
        )
    ]
)
data class TVEpisode (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "show_id")
    var tvShowId: Int,

    @ColumnInfo(name = "season_id", index = true)
    var tvSeasonId: Long,

    @ColumnInfo(name = "season_number")
    var seasonNumber: Int,

    @ColumnInfo(name = "episode_number")
    var episodeNumber: Int,

    @ColumnInfo(name = "air_date")
    var airDate: String,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "still_path")
    var stillPath: String? = null

) : Parcelable {
    override fun toString(): String {
        return "TVEpisode(id=$id, tvShowId=$tvShowId, tvSeasonId=$tvSeasonId, seasonNumber=$seasonNumber, episodeNumber=$episodeNumber, airDate='$airDate', overview='$overview', name='$name', stillPath=$stillPath)"
    }
}