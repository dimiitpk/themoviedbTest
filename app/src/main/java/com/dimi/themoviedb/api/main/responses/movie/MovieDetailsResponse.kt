package com.dimi.themoviedb.api.main.responses.movie

import com.dimi.themoviedb.api.main.responses.tv_show.GenreResponse
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.utils.ConvertUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

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

    @SerializedName("genres")
    @Expose
    var genres: List<GenreResponse>,

    @SerializedName("credits")
    @Expose
    var creditsResponse: CreditsResponse
)
{

    fun toMovie(): Movie {

        val array = ArrayList<Int>()
        for( genre in this.genres) {
            array.add(genre.id)
        }
        return Movie(
            id = id,
            releaseDate = releaseDate,
            popularity = popularity,
            genres = ConvertUtils.fromIntArrayToString(array),
            posterPath = posterPath,
            overview = overview,
            voteAverage = voteAverage,
            voteCount = voteCount,
            title = title,
            castList = toCasts()
        )
    }

    private fun toCasts(): ArrayList<Cast> {
        val castList: ArrayList<Cast> = ArrayList()
        creditsResponse.let {
            for( item in it.cast) {
                val newCast = item.toCast()
                newCast.movieId = id
                castList.add(newCast)
            }
        }
        return castList
    }
}