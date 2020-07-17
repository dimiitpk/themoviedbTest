package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("key")
    @Expose
    var key: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("site")
    @Expose
    var site: String,

    @SerializedName("type")
    @Expose
    var type: String
) : Parcelable
