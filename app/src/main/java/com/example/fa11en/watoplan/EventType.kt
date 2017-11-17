package com.example.fa11en.watoplan

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "eventtype")
data class EventType (@PrimaryKey val name: String,
                      @ColumnInfo val parameters: ArrayList<ParameterTypes>,
                      @ColumnInfo val colorNormal: Int,
                      @ColumnInfo val colorPressed: Int)