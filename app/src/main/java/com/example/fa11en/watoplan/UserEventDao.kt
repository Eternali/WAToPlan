package com.example.fa11en.watoplan

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query


@Dao
interface UserEventDao {

    @Query('SELECT * FROM event')
    val getAll(): UserEvent

}