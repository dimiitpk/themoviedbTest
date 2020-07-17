package com.dimi.themoviedb.api.main.responses.actor

import com.dimi.themoviedb.models.CombinedCredit
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActorKnownForResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("cast")
    @Expose
    var cast: List<ActorCombinedCredits>
)
{
    fun toCombinedCredits(): List<CombinedCredit> {
        val creditList: ArrayList<CombinedCredit> = ArrayList()
        for( item in cast ) {
            val newCast = item.toCredit()
            newCast.actorId = id
            creditList.add(newCast)
        }
        return creditList
    }

    override fun toString(): String {
        return "ActorKnownForResponse(id=$id, cast=$cast)"
    }
}