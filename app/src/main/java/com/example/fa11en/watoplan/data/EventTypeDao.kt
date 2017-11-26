package com.example.fa11en.watoplan

import android.arch.persistence.room.*


@Dao
interface EventTypeDao {

    @Query("SELECT * FROM eventtype")
    fun getAll(): List<EventType>

    @Query("SELECT * FROM eventtype WHERE name = :name")
    fun get(name: String): EventType

    @Insert
    fun insert (vararg events: EventType)

    @Delete
    fun delete (vararg events: EventType)

    @Update
    fun update (vararg events: EventType)


}