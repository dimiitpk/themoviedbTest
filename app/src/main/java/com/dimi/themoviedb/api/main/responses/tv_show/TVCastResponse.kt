package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.models.TVCast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVCastResponse(

    @SerializedName("credit_id")
    @Expose
    var id: String,

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
    fun toCast() : TVCast {
        return TVCast(
            character = character,
            actorId = actorId,
            name = name,
            order = order,
            profile_path = profile_path,
            tvShowId = -1
        )
    }

    override fun toString(): String {
        return "TVCastResponse(id=$id, character='$character', actorId=$actorId, name='$name', order=$order, profile_path=$profile_path)"
    }

}