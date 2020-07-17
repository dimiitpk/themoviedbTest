package com.dimi.themoviedb.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dimi.themoviedb.utils.ConvertUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "actor")
data class Actor(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "gender")
    var gender: Int? = null,

    @ColumnInfo(name = "birthday")
    var birthday: String? = null,

    @ColumnInfo(name = "known_for_department")
    var department: String? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "biography")
    var biography: String? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Float? = null,

    @ColumnInfo(name = "place_of_birth")
    var placeOfBirth: String? = null,

    @ColumnInfo(name = "profile_path")
    var profilePath: String? = null
) : PassingModel(), Parcelable
{
    override fun toString(): String {
        return "Actor(id=$id, gender=$gender, birthday=$birthday, department=$department, name='$name', biography=$biography, popularity=$popularity, placeOfBirth=$placeOfBirth, profilePath=$profilePath)"
    }
}
