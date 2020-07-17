package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "combined_credits",
    foreignKeys = [
        ForeignKey(
            entity = Actor::class,
            parentColumns = ["id"],
            childColumns = ["actor_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CombinedCredit(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "actor_id", index = true)
    var actorId: Int,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "media_type")
    var mediaType: String? = null,

    @ColumnInfo(name = "character")
    var character: String? = null,

    @ColumnInfo(name = "vote_count")
    var voteCount: Int? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float? = null,

    @ColumnInfo(name = "poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "overview")
    var overview: String?
) : Parcelable {

    fun isMovie(): Boolean {
        if (mediaType.equals("movie"))
            return true
        return false
    }

    fun toMovie(): Movie {
        return Movie(
            id = id,
            title = title ?: "",
            popularity = popularity ?: 0.0,
            voteCount = voteCount ?: 0,
            voteAverage = voteAverage ?: 0.0f,
            overview = overview ?: "",
            posterPath = posterPath
        )
    }

    fun toTvShow(): TVShow {
        return TVShow(
            id = id,
            title = name ?: "",
            popularity = popularity ?: 0.0,
            voteCount = voteCount ?: 0,
            voteAverage = voteAverage ?: 0.0f,
            overview = overview ?: "",
            posterPath = posterPath
        )
    }

    fun isTvShow(): Boolean {
        if (mediaType.equals("tv"))
            return true
        return false
    }

    override fun toString(): String {
        return "CombinedCredit(id=$id, actorId=$actorId, title=$title, name=$name, mediaType=$mediaType, character=$character, voteCount=$voteCount, popularity=$popularity, voteAverage=$voteAverage, posterPath=$posterPath)"
    }
}