package com.dimi.themoviedb.api.main.responses.movie

import com.dimi.themoviedb.models.Cast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreditsResponse(

    @SerializedName("cast")
    @Expose
    var cast: List<CastResponse>
)
{

    override fun toString(): String {
        return "CreditsResponse(cast=$cast)"
    }
}