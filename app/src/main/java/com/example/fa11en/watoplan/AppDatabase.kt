package com.example.fa11en.watoplan

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context


@Database(entities = arrayOf(UserEvent::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase () {

    abstract fun eventDao () : UserEventDao

}