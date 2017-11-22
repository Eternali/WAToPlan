package com.example.fa11en.watoplan

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "eventtype")
data class EventType (@PrimaryKey var name: String = "",
                      @ColumnInfo var parameters: MutableList<ParameterTypes> = mutableListOf(),  // the only reason this is mutable is for RoomDB
                      @ColumnInfo var colorNormal: Int = 0,
                      @ColumnInfo var colorPressed: Int = 0) {
    init {  }  // need empty constructor for Room DB
}