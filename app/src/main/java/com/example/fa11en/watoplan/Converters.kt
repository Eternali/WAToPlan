package com.example.fa11en.watoplan

import android.arch.persistence.room.TypeConverter
import java.util.*


object Converters {

    @TypeConverter
    fun fromTimestamp (stamp: Long?) : Calendar? {
        if (stamp != null) {
            val cal = Calendar.getInstance()
            cal.time = Date(stamp)
            return cal
        } else return null
    }

    @TypeConverter
    fun calToTimestamp (cal: Calendar?) : Long? {
        if (cal != null) return cal.timeInMillis
        else return null
    }



}