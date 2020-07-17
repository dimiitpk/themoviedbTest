package com.dimi.themoviedb.api.main.responses.tv_show

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String
)