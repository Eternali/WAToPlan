package com.example.fa11en.watoplan

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface UserEventDao {

    @Query("SELECT * FROM userevent")
    fun getAll(): List<UserEvent>

    @Insert
    fun insert (event: UserEvent)

    @Delete
    fun delete (event: UserEvent)

}