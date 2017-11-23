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
    val types: MutableLiveData<List<EventType>> = MutableLiveData()
    // TODO: possibly make application wide livedata (in its own ViewModel) (events, maybe types?)
    val events: MutableLiveData<List<UserEvent>> = MutableLiveData()

    class Loading private constructor (dbIsLoaded: Boolean = false,
                                       typesAreLoaded: Boolean = false,
                                       eventsAreLoaded: Boolean = false,
                                       fragToDisplay: Int)
        : SummaryViewState() {

        val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val eventsLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val displayFrag: MutableLiveData<Int> = MutableLiveData()

        // initialize variables
        init {
            dbLoaded.postValue(dbIsLoaded)
            typesLoaded.postValue(typesAreLoaded)
            eventsLoaded.postValue(eventsAreLoaded)
            displayFrag.postValue(fragToDisplay)
            types.postValue(mutableListOf())
            events.postValue(mutableListOf())
        }

        companion object {
            private var INSTANCE: Loading? = null
            fun getInstance (dbIsLoaded: Boolean = false,
                             typesAreLoaded: Boolean = false,
                             eventsAreLoaded: Boolean = false,
                             fragToDisplay: Int): Loading {
                if (INSTANCE == null)
                    INSTANCE = Loading(dbIsLoaded, typesAreLoaded, eventsAreLoaded, fragToDisplay)

                return INSTANCE!!
            }
            fun destroyInstance () {
                INSTANCE = null
            }
        }

    }

    class DayViewModel private constructor (p: Int = 0)
        : SummaryViewState() {

        // LiveData objects for day view state
        val pos: MutableLiveData<Int> = MutableLiveData()

        init {
            pos.postValue(p)
        }

        companion object {
            private var INSTANCE: DayViewModel? = null
            fun getInstance (p: Int = 0): DayViewModel {
                if (INSTANCE == null)
                    INSTANCE = DayViewModel(p)

                return INSTANCE!!
            }
            fun destroyInstance () {
                INSTANCE = null
            }
        }

    }

    class WeekViewModel: SummaryViewState() {

    }

    class MonthViewModel: SummaryViewState() {

    }

}
