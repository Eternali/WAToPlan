package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Parcel
import android.os.Parcelable
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.UserEvent


sealed class SummaryViewState: ViewModel () {

    // LiveData objects for all summary states
    val events: MutableLiveData<MutableList<UserEvent>> = MutableLiveData()
    val types: MutableLiveData<MutableList<EventType>> = MutableLiveData()

    class Loading (dbIsLoaded: Boolean = false,
                   typesAreLoaded: Boolean = false,
                   eventsAreLoaded: Boolean = false,
                   fragToDisplay: Int)
        : SummaryViewState() {

        val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val eventsLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val displayFrag: MutableLiveData<Int> = MutableLiveData()

        init {
            dbLoaded.postValue(dbIsLoaded)
            typesLoaded.postValue(typesAreLoaded)
            eventsLoaded.postValue(eventsAreLoaded)
            displayFrag.postValue(fragToDisplay)
        }

    }

    class DayViewModel (p: Int = 0)
        : SummaryViewState(), Parcelable {
        // LiveData objects for day view state
        val pos: MutableLiveData<Int> = MutableLiveData()
        init {
            pos.postValue(p)
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeInt(pos.value!!)
        }
    }

    class WeekViewModel: SummaryViewState() {

    }

    class MonthViewModel: SummaryViewState() {

    }

}
