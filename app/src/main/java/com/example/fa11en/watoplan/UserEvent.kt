package com.example.fa11en.watoplan

import android.arch.persistence.room.*
import android.location.Location
import android.util.EventLog
import java.util.Calendar
import kotlin.collections.HashMap


@Entity(tableName = "userevent")
class UserEvent (@ColumnInfo(name = "typename") val typeName: String) {

    @Ignore
    var type: EventType?

    init {
    }

    @PrimaryKey(autoGenerate = true)
    var eid: Int = 0

    @ColumnInfo(name = "params")
    var params: HashMap<ParameterTypes, Any> = hashMapOf()

    // TODO: add ability to change event type on the fly

    @Ignore
    fun setParam (key: ParameterTypes, value: Any) {
        if (key in type.parameters && checkParamType(key, value))
            this.params.set(key, value)
    }

    companion object {
        fun checkParamType (key: ParameterTypes, value: Any): Boolean {
            return when (key) {
                ParameterTypes.TITLE -> value is String
                ParameterTypes.DESCRIPTION -> value is String
                ParameterTypes.DATETIME -> value is Calendar
                ParameterTypes.LOCATION -> value is Location
                ParameterTypes.ENTITIES -> value is MutableList<*>
                ParameterTypes.REPEAT -> value is MutableList<*>
            }
        }
    }

}