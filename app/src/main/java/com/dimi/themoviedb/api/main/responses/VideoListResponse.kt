package com.dimi.themoviedb.api.main.responses

import com.dimi.themoviedb.models.Video
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoListResponse(

    @SerializedName("results")
    @Expose
    var results: List<Video>
) {
    fun toYoutubeTrailersList(): ArrayList<Video> {
        val trailerList: ArrayList<Video> = ArrayList()
        for (video in results) {
            if (video.site == "YouTube")
                trailerList.add(video)
        }
        return trailerList
    }
}