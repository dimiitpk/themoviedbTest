package com.dimi.themoviedb.api.main.responses.movie

import com.dimi.themoviedb.models.Cast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CastListResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("cast")
    @Expose
    var cast: List<CastResponse>
)
{
    fun toCasts(): List<Cast> {
        val castList: ArrayList<Cast> = ArrayList()
        for( item in cast ) {
            val newCast = item.toCast()
            newCast.movieId = id
            castList.add(newCast)
        }
        return castList
    }

    override fun toString(): String {
        return "CastListResponse(movieId=$id, cast=$cast)"
    }
}