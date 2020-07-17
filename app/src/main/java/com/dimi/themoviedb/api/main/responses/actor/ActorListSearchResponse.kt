package com.dimi.themoviedb.api.main.responses.actor

import com.dimi.themoviedb.models.Actor
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActorListSearchResponse(

    @SerializedName("page")
    @Expose
    var page: Int,

    @SerializedName("total_results")
    @Expose
    var totalResults: Int,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int,

    @SerializedName("results")
    @Expose
    var results: List<ActorSearchResponse>
) {
    fun toActorsList(): List<Actor> {
        val actorsList: ArrayList<Actor> = ArrayList()
        for (actors in results) {
            actorsList.add(
                actors.toActor()
            )
        }
        return actorsList
    }

    override fun toString(): String {
        return "ActorListSearchResponse(page=$page, totalResults=$totalResults, totalPages=$totalPages, results=$results)"
    }


}