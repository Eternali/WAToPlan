package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.*


sealed class SettingsViewState (t: Themes): ViewModel () {

    // LiveData objects for all settings states
    val theme: MutableLiveData<Themes> = MutableLiveData()
    val types: MutableLiveData<List<EventType>> = MutableLiveData()

    init {
        // set or load theme
        when (t) {
            Themes.LIGHT -> {
                theme.postValue(Themes.LIGHT)
            }
            Themes.DARK -> {
                theme.postValue(Themes.DARK)
            }
        }
    }

    class Loading (t: Themes, dbIsLoaded: Boolean = false, typesIsLoaded: Boolean = false, etypes: List<EventType> = mutableListOf())
        : SettingsViewState (t) {

        val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()

        init {
            // database loading status
            dbLoaded.postValue(dbIsLoaded)
            // type loading status (note we can accept etypes argument because only the database might not be loaded)
            typesLoaded.postValue(typesIsLoaded)
            types.postValue(etypes)
        }

    }

    class Passive (t: Themes, etypes: List<EventType> = mutableListOf()): SettingsViewState (t) {

        init {
            // set event types
            types.postValue(etypes)
        }

    }

}