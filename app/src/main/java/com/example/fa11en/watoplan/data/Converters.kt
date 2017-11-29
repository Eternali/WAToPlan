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
    fun fromTimestamp (stamp: Long?): Calendar? {
        return if (stamp != null) {
            val cal = Calendar.getInstance()
            cal.time = Date(stamp)
            cal
        } else null
    }

    fun calToTimestamp (cal: Calendar?): Long? {
        return cal?.timeInMillis
    }

    /* Next is converting between a Location and a String containing Latitude and Longitude */
    fun fromLocstring (locstr: String?): Location? {
        return if (locstr != null) {
            val loc = Location("gps")
            val locArr = locstr.split(',')
            loc.latitude = locArr[0].toDouble()
            loc.longitude = locArr[1].toDouble()

            loc
        } else null
    }

    fun locToStr (loc: Location?): String? {
        return if (loc != null) loc.latitude.toString() + ',' + loc.longitude.toString()
        else null
    }

    /* Next is converting between a list of timestamps and a String */
    fun fromListString (listStr: String?): List<Long>? {
        return listStr?.split(",")?.map { it.toLong() }
    }

    fun toRepeatStr (repeats: MutableList<Long>?): String? {
        return repeats?.joinToString {
            if (repeats.indexOf(it) == repeats.size - 1) it.toString() + ','
            else it.toString()
        }
    }

    /* Convert parameter Hashmap to String */
    @TypeConverter
    fun fromStringtoHashParams (paramStr: String?): HashMap<ParameterTypes, Any>? {
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
                    ParameterTypes.NOTIS.param ->
                        params[ParameterTypes.NOTIS] = stringToNotis(it.split("=")[1])!!
                    ParameterTypes.LOCATION.param ->
                        params[ParameterTypes.LOCATION] = fromLocstring(it.split("=")[1])!!
                    ParameterTypes.ENTITIES.param ->
                        params[ParameterTypes.ENTITIES] = it.split("=")[1]
                    ParameterTypes.REPEAT.param ->
                        params[ParameterTypes.REPEAT] = fromListString(it.split("=")[1])!!
                    ParameterTypes.PROGRESS.param ->
                        params[ParameterTypes.PROGRESS] = it.split("=")[1].toInt()
                    ParameterTypes.PRIORITY.param ->
                        params[ParameterTypes.PRIORITY] = it.split("=")[1].toInt()
                }
            }

            params
        } else null

    }

    @TypeConverter
    fun hashParamstoString (params: HashMap<ParameterTypes, Any>?): String? {
        return if (params != null) {
            var encodedStr = ""

            // TODO: encode params better: eg. at start of new param, put length of the param to avoid special character confusion
            params.keys.forEach{
                if (encodedStr.isNotEmpty()) encodedStr += "&"
                encodedStr += it.toString() + "="
                encodedStr += when (it) {
                    ParameterTypes.TITLE -> params[it]
                    ParameterTypes.DESCRIPTION -> params[it]
                    ParameterTypes.DATETIME -> calToTimestamp(params[it] as Calendar).toString()
                    ParameterTypes.NOTIS -> notisToString(params[it] as MutableList<Int>)
                    ParameterTypes.LOCATION -> locToStr(params[it] as Location)
                    ParameterTypes.ENTITIES -> params[it]
                    ParameterTypes.REPEAT -> toRepeatStr(params[it] as MutableList<Long>)
                    ParameterTypes.PROGRESS -> params[it] as Double
                    ParameterTypes.PRIORITY -> params[it] as Int
                }
            }

            encodedStr
        } else null
    }

    /* EventType parameters to encoded string */
    @TypeConverter
    fun paramsToString (parameters: MutableList<ParameterTypes>?): String? =
            parameters?.joinToString("&", transform = { it -> it.param})

    /* Encoded string to event parameters */
    @TypeConverter
    fun stringToParams (encodedStr: String?): List<ParameterTypes>? =
            encodedStr?.split('&')?.map { paramToParamType(it) }


    /* Notification list as string */
    @TypeConverter
    fun notisToString (notiIds: MutableList<Int>?): String? {
        return notiIds?.map { it.toString() }?.joinToString(",") ?: ""
    }

    /* Encoded string to noti list */
    @TypeConverter
    fun stringToNotis (encodedStr: String?): List<Int>? {
        return encodedStr?.split(",")?.map { it.toInt() }
    }

}