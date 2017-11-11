package com.example.fa11en.watoplan

import android.arch.persistence.room.TypeConverter
import android.location.Location
import java.util.*


// This is a list of converters to convert complex datatypes (objects) into basic datatypes for
// storage in a SQLite database.
object Converters {

    /* First is converting between Calendar and a long timestamp */
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

    /* Next is converting between a Location and a String containing Latitude and Longitude */
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

    /* Next is converting between a list of timestamps and a String */
    @TypeConverter
    fun fromListString (listStr: String?) : MutableList<Long>? {
        return if (listStr != null) {

        } else null
    }

    @TypeConverter
    fun toRepeatStr (repeats: MutableList<Long>?) : String? {
        return if (repeats != null) {
            
        } else null
    }

}