package com.example.fa11en.watoplan

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


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
    fun writeEventAndReadInList () {
        val event = UserEvent(0, eventTypes["EVENT"])
        event.setParams
    }

}