package com.dimi.themoviedb.api.main.responses.movie

import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.utils.ConvertUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Double,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("genre_ids")
    @Expose
    var genres: List<Int>? = null

) {

    fun toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            popularity = popularity,
            voteCount = voteCount,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            overview = overview,
            posterPath = posterPath,
            genres = ConvertUtils.fromIntArrayToString(genres!!.toMutableList() as ArrayList<Int>)
        )
    }

    override fun toString(): String {
        return "MovieSearchResponse(id=$id, name='$title', popularity=$popularity, voteCount=$voteCount, releaseDate=$releaseDate, voteAverage=$voteAverage, overview='$overview', posterPath=$posterPath, genres=$genres)"
    }


}