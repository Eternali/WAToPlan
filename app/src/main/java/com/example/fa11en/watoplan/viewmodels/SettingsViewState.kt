package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.*


class SettingsViewState: ViewModel () {

    // LiveData objects for all settings states
    val theme: MutableLiveData<Themes> = MutableLiveData()

    init {
        // set or load theme
        theme.postValue(Themes.LIGHT)
    }

}