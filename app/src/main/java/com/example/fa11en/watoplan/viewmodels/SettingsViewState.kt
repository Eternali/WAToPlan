package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.Themes


sealed class SettingsViewState (t: Themes, dbl: Boolean = false, tl: Boolean = false, etypes: MutableList<EventType>? = null): ViewModel () {

    // LiveData objects for all settings states
    val theme: MutableLiveData<Themes> = MutableLiveData()
    val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val types: MutableLiveData<MutableList<EventType>> = MutableLiveData()

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

        // check database loading
        dbLoaded.postValue(dbl)

        // load types
        typesLoaded.postValue(tl)
    }

}