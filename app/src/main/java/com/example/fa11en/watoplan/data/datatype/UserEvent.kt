package com.example.fa11en.watoplan

import android.arch.persistence.room.*
import android.location.Location
import android.util.EventLog
import java.util.Calendar
import kotlin.collections.HashMap


@Entity(tableName = "userevent")
class UserEvent (@ColumnInfo(name = "typename") val typeName: String) {

    @Ignore
    var type: EventType? = null

    constructor (eventType: EventType): this(eventType.name) {
        type = eventType
    }

    @PrimaryKey(autoGenerate = true)
    var eid: Int = 0

    @ColumnInfo(name = "params")
    var params: HashMap<ParameterTypes, Any> = hashMapOf()

    // TODO: add ability to change event type on the fly

    // will return false if type hasn't been loaded yet or an error occurs
    @Ignore
    fun setParam (key: ParameterTypes, value: Any): Boolean {
        return if (type != null && key in type!!.parameters && checkParamType(key, value)) {
            this.params[key] = value
            true
        } else false
    }

    @Ignore
    fun loadType (db: AppDatabase): Boolean {
        db.beginTransaction()
        return try {
            type = db.typeDao().get(typeName)
            true
        } catch (e: Exception) {
            false
        } finally {
            db.endTransaction()
        }
    }

    companion object {
        fun checkParamType (key: ParameterTypes, value: Any): Boolean = when (key) {
            ParameterTypes.TITLE -> value is String
            ParameterTypes.DESCRIPTION -> value is String
            ParameterTypes.DATETIME -> value is Calendar
            ParameterTypes.NOTIS -> value is MutableList<*>
            ParameterTypes.LOCATION -> value is Location
            ParameterTypes.ENTITIES -> value is MutableList<*>
            ParameterTypes.REPEAT -> value is MutableList<*>
            ParameterTypes.PROGRESS -> value is Double && value >= 0 && value <= 100
            ParameterTypes.PRIORITY -> value is Int
        }
    }

}