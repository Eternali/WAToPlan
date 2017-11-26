package com.example.fa11en.watoplan

import android.arch.persistence.room.*


@Dao
interface UserEventDao {

    @Query("SELECT * FROM userevent")
    fun getAll(): List<UserEvent>

    @Query("SELECT * FROM userevent WHERE eid = :eid")
    fun get(eid: Int): UserEvent

    @Insert
    fun insert (vararg events: UserEvent)

    @Delete
    fun delete (vararg events: UserEvent)

    @Update
    fun update (vararg events: UserEvent)

}