package com.chipthink.eternali.watoplan.viewmodels

import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences


class SettingsViewState: ViewModel () {

    // LiveData objects for all settings states
    // theme is saved here as well
    var sharedPref: SharedPreferences? = null
    // preference change listener
    var prefListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    init {
        // set or load theme
//        theme.postValue(Themes.LIGHT)
    }

}