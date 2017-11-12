package com.example.fa11en.watoplan

import android.arch.persistence.room.Room
import android.location.Location
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class UserEventReadWriteTest {

    private lateinit var userEventDao: UserEventDao
    private lateinit var mDb: AppDatabase

    @Before
    fun createDb () {
        val ctx = InstrumentationRegistry.getTargetContext()
        mDb = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        userEventDao = mDb.eventDao()
    }

    @After
    fun closeDb () {
        mDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeEventAndReadInList () {
        val event = UserEvent(0, eventTypes["EVENT"]!!)
        event.setParam(ParameterTypes.TITLE, "TEST TITLE")
        event.setParam(ParameterTypes.DESCRIPTION, "TEST DESCRIPTION")
        event.setParam(ParameterTypes.DATETIME, Calendar.getInstance())
        event.setParam(ParameterTypes.LOCATION, Location("gps"))
        userEventDao.insert(event)
        val gets: List<UserEvent> = userEventDao.getAll()
        assert(gets[0] == event)
    }

}