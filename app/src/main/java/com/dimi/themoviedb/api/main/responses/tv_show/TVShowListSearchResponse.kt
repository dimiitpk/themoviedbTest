package com.dimi.themoviedb.api.main.responses.tv_show

import com.dimi.themoviedb.models.TVShow
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVShowListSearchResponse(

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
    var results: List<TVShowSearchResponse>
) {
    fun toTVShowList() : List<TVShow> {
        val tvShowList: ArrayList<TVShow> = ArrayList()
        for( tvShow in results){
            tvShowList.add(
                tvShow.toTVShow()
            )
        }
        return tvShowList
    }

    override fun toString(): String {
        return "MovieListSearchResponse(page=$page, totalResults=$totalResults, totalPages=$totalPages, results=$results)"
    }

}