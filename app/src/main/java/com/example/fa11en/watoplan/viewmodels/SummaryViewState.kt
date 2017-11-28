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

    class Loading private constructor (dbIsLoaded: Boolean = false,
                                       typesAreLoaded: Boolean = false,
                                       eventsAreLoaded: Boolean = false)
        : SummaryViewState() {

        val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val eventsLoaded: MutableLiveData<Boolean> = MutableLiveData()

        // initialize variables
        init {
            dbLoaded.postValue(dbIsLoaded)
            typesLoaded.postValue(typesAreLoaded)
            eventsLoaded.postValue(eventsAreLoaded)
        }

        companion object {
            private var INSTANCE: Loading? = null
            fun getInstance (dbIsLoaded: Boolean = false,
                             typesAreLoaded: Boolean = false,
                             eventsAreLoaded: Boolean = false): Loading {
                if (INSTANCE == null)
                    INSTANCE = Loading(dbIsLoaded, typesAreLoaded, eventsAreLoaded)

                return INSTANCE!!
            }
            fun destroyInstance () {
                INSTANCE = null
            }
        }

    }

    class Passive constructor (fragToDisplay: Int): SummaryViewState() {

        val displayFrag: MutableLiveData<Int> = MutableLiveData()
        val pos: MutableLiveData<Int> = MutableLiveData()

        init {
            displayFrag.postValue(fragToDisplay)
            pos.postValue(0)
        }

    }

    companion object {
        val events: MutableLiveData<List<UserEvent>> = MutableLiveData()
    }

}
