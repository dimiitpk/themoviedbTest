package com.dimi.themoviedb.api.main.responses.movie

import com.dimi.themoviedb.models.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieListSearchResponse(

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
    var results: List<MovieSearchResponse>
) {
    fun toMovieList() : List<Movie> {
        val moviesList: ArrayList<Movie> = ArrayList()
        for( movie in results){
            moviesList.add(
                movie.toMovie()
            )
        }
        return moviesList
    }

    override fun toString(): String {
        return "MovieListSearchResponse(page=$page, totalResults=$totalResults, totalPages=$totalPages, results=$results)"
    }

}