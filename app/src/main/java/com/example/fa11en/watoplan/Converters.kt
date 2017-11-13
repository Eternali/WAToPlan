package com.example.fa11en.watoplan

import android.arch.persistence.room.TypeConverter
import android.location.Location
import android.util.Log
import java.util.*
import kotlin.collections.HashMap


// This is a list of converters to convert complex datatypes (objects) into basic datatypes for
// storage in a SQLite database.
class Converters {

    /* First is converting between Calendar and a long timestamp */
    fun fromTimestamp (stamp: Long?) : Calendar? {
        return if (stamp != null) {
            val cal = Calendar.getInstance()
            cal.time = Date(stamp)
            cal
        } else null
    }

    fun calToTimestamp (cal: Calendar?) : Long? {
        return cal?.timeInMillis
    }

    /* Next is converting between a Location and a String containing Latitude and Longitude */
    fun fromLocstring (locstr: String?) : Location? {
        return if (locstr != null) {
            val loc = Location("gps")
            val locArr = locstr.split(',')
            loc.latitude = locArr[0].toDouble()
            loc.longitude = locArr[1].toDouble()

            loc
        } else null
    }

    fun locToStr (loc: Location?) : String? {
        return if (loc != null) loc.latitude.toString() + ',' + loc.longitude.toString()
        else null
    }

    /* Next is converting between a list of timestamps and a String */
    fun fromListString (listStr: String?) : List<Long>? {
        return listStr?.split(",")?.map { it.toLong() }
    }

    fun toRepeatStr (repeats: MutableList<Long>?) : String? {
        return repeats?.joinToString { it.toString() + ','}
    }

    /* Convert parameter Hashmap to String */
    @TypeConverter
    fun fromStringtoParams (paramStr: String?) : HashMap<ParameterTypes, Any>? {
        return if (paramStr != null) {
            val params: HashMap<ParameterTypes, Any> = hashMapOf()

            paramStr.split("&").forEach {
                if (it.split("=").size != 2) return@forEach
                when (it.split("=")[0]) {
                    ParameterTypes.TITLE.param ->
                        params[ParameterTypes.TITLE] = it.split("=")[1]
                    ParameterTypes.DESCRIPTION.param ->
                        params[ParameterTypes.DESCRIPTION] = it.split("=")[1]
                    ParameterTypes.DATETIME.param ->
                        params[ParameterTypes.DATETIME] = fromTimestamp(it.split("=")[1].toLong())!!
                    ParameterTypes.LOCATION.param ->
                        params[ParameterTypes.LOCATION] = fromLocstring(it.split("=")[1])!!
                    ParameterTypes.ENTITIES.param ->
                        params[ParameterTypes.ENTITIES] = it.split("=")[1]
                    ParameterTypes.REPEAT.param ->
                        params[ParameterTypes.REPEAT] = fromListString(it.split("=")[1])!!
                }
            }

            params
        } else null

    }

    @TypeConverter
    fun hashParamstoString (params: HashMap<ParameterTypes, Any>?) : String? {
        return if (params != null) {
            var encodedStr = ""

            // TODO: figure out a better way to typecheck ParameterTypes enum vals
            params.keys.forEach{
                if (encodedStr.isNotEmpty()) encodedStr += "&"
                encodedStr += it.toString() + "="
                encodedStr += when (it) {
                    ParameterTypes.TITLE -> params[it]
                    ParameterTypes.DESCRIPTION -> params[it]
                    ParameterTypes.DATETIME -> calToTimestamp(params[it] as Calendar).toString()
                    ParameterTypes.LOCATION -> locToStr(params[it] as Location)
                    ParameterTypes.ENTITIES -> params[it]
                    ParameterTypes.REPEAT -> toRepeatStr(params[it] as MutableList<Long>)
                }
            }

            Log.i("HASHEDSTR", encodedStr)

            encodedStr
        } else null
    }

    /* Convert EventType to String */
    @TypeConverter
    fun stringToEvent (eventStr: String?) : EventType? {
        if (eventStr != null) {
            eventTypes.forEach {
                if (it.value.name == eventStr) return it.value
            }
        }
        return null
    }

    @TypeConverter
    fun eventToString (event: EventType?) : String? {
        return event?.name
    }

}