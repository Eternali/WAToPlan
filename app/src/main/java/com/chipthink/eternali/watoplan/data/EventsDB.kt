package com.chipthink.eternali.watoplan

import android.arch.persistence.room.Room
import android.content.Context


class EventsDB (ctx: Context) {

    var appDatabase: AppDatabase

    init {
        appDatabase = Room.databaseBuilder(ctx, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries().build()
    }

    companion object {
        private val DATABASE_NAME = "app-database"
        private var INSTANCE: EventsDB? = null
        fun getInstance (ctx: Context) : AppDatabase {
            if (INSTANCE == null)
                INSTANCE = EventsDB(ctx)

            return (INSTANCE as EventsDB).appDatabase
        }
        fun destroyInstance () {
            INSTANCE = null
        }
    }

}