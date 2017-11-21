package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.*


sealed class SettingsViewState (t: Themes): ViewModel () {

    // LiveData objects for all settings states
    val theme: MutableLiveData<Themes> = MutableLiveData()
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
    }

    class Loading (t: Themes, dbl: Boolean = false, tl: Boolean = false, etypes: MutableList<EventType>? = null)
        : SettingsViewState (t) {

        val dbLoaded: MutableLiveData<Boolean> = MutableLiveData()
        val typesLoaded: MutableLiveData<Boolean> = MutableLiveData()

        init {
            // database loading status
            dbLoaded.postValue(dbl)
            // type loading status (note we can accept etypes argument because only the database might not be loaded)
            typesLoaded.postValue(tl)
            if (etypes != null) types.value = etypes
        }

    }

    class Passive (t: Themes, etypes: MutableList<EventType> = mutableListOf()): SettingsViewState (t) {

        init {
            // set events
            types.postValue(etypes)
        }

    }

    class Editing (t: Themes, etype: EventType? = null): SettingsViewState (t) {

        val type: MutableLiveData<EventType> = MutableLiveData()
        val typeName: MutableLiveData<String> = MutableLiveData()
        val typeParams: HashMap<ParameterTypes, MutableLiveData<Boolean>> = hashMapOf()
        val typeColorNormal: MutableLiveData<Int> = MutableLiveData()
        val typeColorPressed: MutableLiveData<Int> = MutableLiveData()

        init {
            // set type
            // other classes (i.e. edit dialog) should observe this and adjust editables accordingly
            if (etype != null) type.postValue(etype)

            // initialize all parameters to false
            ParameterTypes.values().forEach {
                typeParams[it] = MutableLiveData()
                typeParams[it]!!.postValue(false)  // WATCH NULL-SAFE ASSERTION
            }
            // set defaults
            typeParams[ParameterTypes.TITLE]!!.postValue(true)
            typeParams[ParameterTypes.DESCRIPTION]!!.postValue(true)

            // initialize other event type parameters
            typeName.postValue("")
            typeColorNormal.postValue(R.color.colorAccent)
            typeColorPressed.postValue(R.color.colorAccent_pressed)
        }

    }

}