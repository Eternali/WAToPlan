package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.UserEvent


sealed class SummaryViewState: ViewModel () {
    // LiveData objects for all summary states
    val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val events: MutableLiveData<MutableList<UserEvent>> = MutableLiveData()
    val eventsLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val types: MutableLiveData<MutableList<EventType>> = MutableLiveData()
    val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()

    class DayViewModel (p: Int = 0, dbIsLoaded: Boolean = false, typesAreLoaded: Boolean = false, eventsAreLoaded: Boolean = false)
        : SummaryViewState() {
        // LiveData objects for day view state
        val pos: MutableLiveData<Int> = MutableLiveData()
        init {
            pos.postValue(p)
            dbLoaded.postValue(dbIsLoaded)
            typesLoaded.postValue(typesAreLoaded)
            eventsLoaded.postValue(eventsAreLoaded)
        }
    }

    class WeekViewModel: SummaryViewState() {

    }

    class MonthViewModel: SummaryViewState() {

    }

}
