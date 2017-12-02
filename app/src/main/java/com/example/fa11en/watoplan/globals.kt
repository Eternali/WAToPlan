package com.example.fa11en.watoplan

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import java.util.*


// global enum of themes
enum class Themes {
    DARK,
    LIGHT
}

// global enum for request codes
enum class RequestCodes (val code: Int) {
    NEWEVENTTYPE(100),
    EDITEVENTTYPE(101),
    NEWEVENT(103),
    EDITEVENT(104),
    ISEVENTTYPECHANGED(105)
}

// global enum for result codes
enum class ResultCodes (val code: Int) {
    TYPECANCELED(200),
    TYPESAVED(201),
    TYPEFAILED(202),
    TYPEDELETED(203),
    TYPECHANGED(204),
    EVENTCANCELED(205),
    EVENTSAVED(206),
    EVENTFAILED(207),
    EVENTDELETED(208),
    EVENTCHANGED(209)
}

/**
 * TO ADD A NEW PARAMETER TYPE AND IMPLEMENT IT, DO THE FOLLOWING:
 * 1. append to ParameterTypes enum with a param value of the UI displayable string
 * 2. add an entry in the when statement of UserEvent's setParam method
 * 3. add a TypeConverter in Converters.kt for the hashmap of ParameterTypes to Any
 * 4. add a variable in EditViewState to hold its editable value.
 *      NOTE: this is required because I cannot use LiveData.postValue on vars of type Any or *
 * 5. Add a Checkbox in edittype_layout.xml
 * 6. Add the edit UI element in activity_edit.xml
 * 7.
 */
enum class ParameterTypes (val param: String) {
    TITLE ("TITLE"),
    DESCRIPTION ("DESCRIPTION"),
    DATETIME ("DATETIME"),
    NOTIS ("NOTIFICATIONS"),
    LOCATION ("LOCATION"),
    PROGRESS ("PROGRESS"),
    PRIORITY ("PRIORITY"),
    ENTITIES ("PEOPLE"),
    REPEAT ("REPETITIONS")
}

// notification types supported
enum class NotiTypes (val title: String) {
    NOTIFICATION ("NOTIFICATION"),
    EMAIL ("EMAIL"),
    SMS ("SMS")
}


////**** EXTENSION FUNCTIONS ****////


// Location extension methods to generate printable text
fun Calendar.timestr (): String {
    return this.get(Calendar.HOUR_OF_DAY).toString() + ": " + this.get(Calendar.MINUTE)
}

fun Calendar.datestr (): String {
    return this.get(Calendar.DAY_OF_MONTH).toString() + " " + this.get(Calendar.MONTH) + " " + this.get(Calendar.YEAR)
}


// add extension to Int to change its range
fun Int.toRange (instart: Int, inend: Int, outstart: Int, outend: Int): Int {
    return outstart + ((outend - outstart) / (inend - instart)) * (this - instart)
}


// event list extensions for sorting

fun pivot (toSort: MutableList<Pair<Int, Long>>, lo: Int, hi: Int): Int {
    val p = toSort[hi]
    var i = lo - 1

    for (j in lo until hi) {
        if (toSort[j].second < p.second) {
            i += 1
            val tmp = toSort[i]
            toSort[i] = toSort[j]
            toSort[j] = tmp
        }
    }
    if (toSort[hi].second < toSort[i+1].second) {
        val tmp = toSort[i+1]
        toSort[i+1] = toSort[hi]
        toSort[hi] = tmp
    }

    return i + 1
}

fun quickSort (toSort: MutableList<Pair<Int, Long>>, lo: Int, hi: Int) {
    if (lo < hi) {
        val p = pivot(toSort, lo, hi)
        quickSort(toSort, lo, p - 1)
        quickSort(toSort, p + 1, hi)
    }
}

fun Pair<Int, Long>.getEvent (events: List<UserEvent>): UserEvent? {
    for (event in events)
        if (event.eid == this.first)
            return event

    return null
}

fun List<UserEvent>.orderByDate (): List<UserEvent?>? {
    val tmpevents: MutableList<Pair<Int, Long>>? = this.map {
        it.eid to if (it.params[ParameterTypes.DATETIME] != null) (it.params[ParameterTypes.DATETIME] as Calendar).timeInMillis else 0
    }.toMutableList()

    if (tmpevents != null) quickSort(tmpevents, 0, tmpevents.size-1)
    return tmpevents?.map { it.getEvent(this) }
}

fun List<UserEvent>.orderByPriority (): List<UserEvent?>? {
    val tmpevents: MutableList<Pair<Int, Long>>? = this.map {
        it.eid to if (it.params[ParameterTypes.PRIORITY] != null) (it.params[ParameterTypes.PRIORITY] as Int).toLong() else 0
    }.toMutableList()

    if (tmpevents != null) quickSort(tmpevents, 0, tmpevents.size-1)
    return tmpevents?.map { it.getEvent(this) }
}

fun List<UserEvent>.orderByProgress (): List<UserEvent?>? {
    val tmpevents: MutableList<Pair<Int, Long>>? = this.map {
        it.eid to if (it.params[ParameterTypes.PROGRESS] != null) (it.params[ParameterTypes.PROGRESS] as Int).toLong() else 0
    }.toMutableList()

    if (tmpevents != null) quickSort(tmpevents, 0, tmpevents.size-1)
    return tmpevents?.map { it.getEvent(this) }
}


// This must be defined to get ParameterTypes object based on its param
fun paramToParamType (param: String): ParameterTypes {
    ParameterTypes.values()
            .filter { it.param == param }
            .forEach { return it }
    throw TypeNotPresentException(param, Throwable())
}

