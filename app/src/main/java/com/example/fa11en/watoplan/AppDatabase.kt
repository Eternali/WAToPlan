package com.example.fa11en.watoplan

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities = {UserEvent.class}, version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract userEventDao () : UserEventDao = 

}