package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.models.Cast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVCreditsResponse(

    @SerializedName("cast")
    @Expose
    var cast: List<TVCastResponse>
)
{

    override fun toString(): String {
        return "CreditsResponse(cast=$cast)"
    }
}