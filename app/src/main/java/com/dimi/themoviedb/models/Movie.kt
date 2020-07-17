package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.*
import com.dimi.themoviedb.utils.ConvertUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie @Ignore constructor(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "popularity")
    var popularity: Double,

    @ColumnInfo(name = "vote_count")
    var voteCount: Int,

    @ColumnInfo(name = "release_date")
    var releaseDate: String? = null,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "genre_ids")
    var genres: String? = null,

    @Ignore
    var castList: ArrayList<Cast> = ArrayList()

) : PassingModel(), Parcelable
{

    constructor(
        id: Int,
        title: String,
        popularity: Double,
        voteCount: Int,
        releaseDate: String?,
        voteAverage: Float,
        overview: String,
        posterPath: String?,
        genres: String?
    ) : this(
        id, title, popularity, voteCount, releaseDate, voteAverage, overview, posterPath, genres, ArrayList()
    )

    override fun toString(): String {
        return "Movie(id=$id, title='$title', popularity=$popularity, voteCount=$voteCount, releaseDate=$releaseDate, voteAverage=$voteAverage, overview='$overview', posterPath=$posterPath, genres=$genres, castList=$castList)"
    }


}