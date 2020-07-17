package com.dimi.themoviedb.api.main.responses.actor

import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.models.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActorSearchResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("gender")
    @Expose
    var gender: Int? = null,

    @SerializedName("known_for_department")
    @Expose
    var department: String? = null,

    @SerializedName("place_of_birth")
    @Expose
    var placeOfBirth: String? = null,

    @SerializedName("biography")
    @Expose
    var biography: String? = null,

    @SerializedName("birthday")
    @Expose
    var birthday: String? = null,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Float? = null,

    @SerializedName("profile_path")
    @Expose
    var profilePath: String? = null
) {

    fun toActor(): Actor {
        return Actor(
            id = id,
            gender = gender,
            name = name,
            profilePath = profilePath,
            department = department,
            popularity = popularity,
            placeOfBirth = placeOfBirth,
            biography = biography,
            birthday = birthday
        )
    }

    override fun toString(): String {
        return "ActorSearchResponse(id=$id, gender=$gender, department=$department, placeOfBirth=$placeOfBirth, biography=$biography, birthday=$birthday, name='$name', popularity=$popularity, profilePath=$profilePath)"
    }


}