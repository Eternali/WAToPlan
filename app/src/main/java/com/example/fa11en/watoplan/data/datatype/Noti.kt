package com.example.fa11en.watoplan

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "noti")
data class Noti (@PrimaryKey(autoGenerate = true) val nid: Int = 0,
                 val time: Calendar,
                 val type: NotiTypes,
                 val title: String,
                 val contents: String)