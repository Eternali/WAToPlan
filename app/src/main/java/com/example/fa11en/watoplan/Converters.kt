package com.example.fa11en.watoplan

import android.arch.persistence.room.TypeConverter
import android.location.Location
import java.util.*


object Converters {

    @TypeConverter
    fun fromTimestamp (stamp: Long?) : Calendar? {
        return if (stamp != null) {
            val cal = Calendar.getInstance()
            cal.time = Date(stamp)
            cal
        } else null
    }

    @TypeConverter
    fun calToTimestamp (cal: Calendar?) : Long? {
        return cal?.timeInMillis
    }

    @TypeConverter
    fun fromLocstring (locstr: String?) : Location? {
        return if (locstr != null) {
            val loc = Location("gps")
            val locArr = locstr.split(',')
            loc.latitude = locArr[0].toDouble()
            loc.longitude = locArr[1].toDouble()

            loc
        } else null
    }

    @TypeConverter
    fun locToStr (loc: Location?) : String? {
        return if (loc != null) loc.latitude.toString() + ',' + loc.longitude.toString()
        else null
    }

}