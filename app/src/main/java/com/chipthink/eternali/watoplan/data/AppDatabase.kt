package com.chipthink.eternali.watoplan

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters


@Database(entities = arrayOf(UserEvent::class, EventType::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase () {

    abstract fun eventDao () : UserEventDao

    abstract fun typeDao () : EventTypeDao

}