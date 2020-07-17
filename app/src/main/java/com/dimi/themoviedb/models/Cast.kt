package com.dimi.themoviedb.models

import android.os.Parcelable
import android.util.Log
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "movie_cast",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = CASCADE
        )
    ],
    primaryKeys = [
        "movieId", "actorId", "character"
    ]
)
// response from api do not have unique id for casts
// so combine "movieId", "actorId", "character" im composite primary key to get a unique one
data class Cast (

    @ColumnInfo(name = "character")
    var character: String,

    @ColumnInfo(name = "movieId")
    var movieId: Int,

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
        return "Cast(character='$character', movieId=$movieId, actorId=$actorId, name='$name', order=$order, profile_path=$profile_path)"
    }
}