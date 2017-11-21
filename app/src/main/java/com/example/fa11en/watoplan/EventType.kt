package com.example.fa11en.watoplan

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "eventtype")
data class EventType (@PrimaryKey val name: String = "",
                      @ColumnInfo val parameters: List<ParameterTypes> = listOf(),
                      @ColumnInfo val colorNormal: Int = 0,
                      @ColumnInfo val colorPressed: Int = 0) {
    init {  }  // need empty constructor for Room DB
}