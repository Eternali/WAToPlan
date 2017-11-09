package com.example.fa11en.watoplan

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.location.Location
import java.util.Calendar
import kotlin.collections.HashMap


@Entity
class UserEvent (@PrimaryKey val eid: Int, val type: EventType) {

    @ColumnInfo(name = "params")
    var params: HashMap<ParameterTypes, Any> = hashMapOf()

    fun setParam (key: ParameterTypes, value: Any) {
        if (key in type.parameters && checkParamType(key, value))
            this.params.set(key, value)
    }

    companion object {
        fun checkParamType (key: ParameterTypes, value: Any): Boolean {
            when (key) {
                ParameterTypes.TITLE -> return value is String
                ParameterTypes.DESCRIPTION -> return value is String
                ParameterTypes.DATETIME -> return value is Calendar
                ParameterTypes.LOCATION -> return value is Location
                ParameterTypes.ENTITIES -> return value is MutableList<*>
                ParameterTypes.REPEAT -> return value is MutableList<*>
            }
        }
    }

}