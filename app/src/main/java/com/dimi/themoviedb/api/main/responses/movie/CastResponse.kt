package com.dimi.themoviedb.api.main.responses.movie

import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.models.Cast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CastResponse(

    @SerializedName("cast_id")
    @Expose
    var id: Int,

    @SerializedName("character")
    @Expose
    var character: String,

    @SerializedName("id")
    @Expose
    var actorId: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("order")
    @Expose
    var order: Int,

    @SerializedName("profile_path")
    @Expose
    var profile_path: String? = null
) {
    fun toCast() : Cast {
        return Cast(
            character = character,
            actorId = actorId,
            name = name,
            order = order,
            profile_path = profile_path,
            movieId = -1
        )
    }

    override fun toString(): String {
        return "CastResponse(id=$id, character='$character', actorId=$actorId, name='$name', order=$order, profile_path='$profile_path')"
    }
}