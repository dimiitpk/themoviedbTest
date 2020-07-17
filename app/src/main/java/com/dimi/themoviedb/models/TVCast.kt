package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "tv_cast",
    foreignKeys = [
        ForeignKey(
            entity = TVShow::class,
            parentColumns = ["id"],
            childColumns = ["tvShowId"],
            onDelete = CASCADE
        )
    ],
    primaryKeys = [
        "tvShowId", "actorId", "character"
    ]
)
// response from api do not have unique id for casts
// so combine "tvShowId", "actorId", "character" im composite primary key to get a unique one
data class TVCast (

    @ColumnInfo(name = "character")
    var character: String,

    @ColumnInfo(name = "tvShowId")
    var tvShowId: Int,

    @ColumnInfo(name = "actorId")
    var actorId: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "order")
    var order: Int,

    @ColumnInfo(name = "profile_path")
    var profile_path: String? = null

) : Parcelable {

    fun toActor() : Actor {
        return Actor(
            name = name,
            id = actorId
        )
    }

    override fun toString(): String {
        return "TVCast(character='$character', tvShowId=$tvShowId, actorId=$actorId, name='$name', order=$order, profile_path=$profile_path)"
    }
}