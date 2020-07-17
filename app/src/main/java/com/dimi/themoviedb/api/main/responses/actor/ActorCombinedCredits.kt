package com.dimi.themoviedb.api.main.responses.actor

import com.dimi.themoviedb.models.CombinedCredit
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActorCombinedCredits(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("media_type")
    @Expose
    var mediaType: String? = null,

    @SerializedName("character")
    @Expose
    var character: String? = null,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null,

    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float? = null,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("overview")
    @Expose
    var overview: String?

) {

    fun toCredit(): CombinedCredit = CombinedCredit(
        id = id,
        actorId = 0,
        title = title,
        name = name,
        mediaType = mediaType,
        voteCount = voteCount,
        popularity = popularity,
        voteAverage = voteAverage,
        character = character,
        posterPath = posterPath,
        overview = overview
    )

    override fun toString(): String {
        return "ActorCombinedCredits(id=$id, title=$title, name=$name, mediaType=$mediaType, character=$character, voteCount=$voteCount, popularity=$popularity, voteAverage=$voteAverage, posterPath=$posterPath)"
    }


}