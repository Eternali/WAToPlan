package com.example.fa11en.watoplan.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.UserEvent


class UserEventViewModel (private val db: AppDatabase) : ViewModel () {

    private var events: MutableLiveData<MutableList<UserEvent>>
        get() = this
        set(value) { events.postValue(events.value) }
    private var curEvent: MutableLiveData<UserEvent> = MutableLiveData()

    init {
        // get all stored data on object creation
        db.beginTransaction()
        try {
            events.postValue(db.eventDao().getAll().toMutableList())
            db.setTransactionSuccessful()
        } catch (e: Exception) {

        } finally {
            db.endTransaction()
        }
    }

    fun getEvents () : MutableLiveData<MutableList<UserEvent>> {
        return events
    }



}