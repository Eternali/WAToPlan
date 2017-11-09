package com.example.fa11en.watoplan

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query


@Dao
interface UserEventDao {

    /* eid,  */

    @Query('SELECT * FROM event')
    val getAll(): List<UserEvent>

    @Query('SELECT * FROM event WHERE eid IN (:eventIds)')
    val loadAllByIds(userIds: Array<Int>) : List<UserEvent>

}