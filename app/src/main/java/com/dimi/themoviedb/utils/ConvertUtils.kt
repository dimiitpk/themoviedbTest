package com.dimi.themoviedb.utils

import android.util.Log
import androidx.room.TypeConverter
import com.dimi.themoviedb.models.TVCast
import com.dimi.themoviedb.models.Video

class ConvertUtils {

    companion object {

        @TypeConverter
        fun fromIntArrayToString(intArray: ArrayList<Int>): String {
            val outPut: StringBuilder = StringBuilder()
            for (item in intArray.indices) {
                outPut.append(intArray[item])
                if (item != intArray.lastIndex) outPut.append(",")
            }
            return outPut.toString()
        }

        @TypeConverter
        fun fromStringToIntArray(input: String): ArrayList<Int> {
            val stringArray: Array<String> = input.split(",").toTypedArray()
            val outPut = ArrayList<Int>()
            for (i in stringArray) {
                outPut.add(i.toInt())
            }
            return outPut
        }
    }


}