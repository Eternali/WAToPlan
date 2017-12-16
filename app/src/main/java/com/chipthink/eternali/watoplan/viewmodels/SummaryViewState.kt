package com.chipthink.eternali.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.getbase.floatingactionbutton.FloatingActionButton


class SummaryViewState: ViewModel () {

    val displayFrag = MutableLiveData<Int>()
    val pos = MutableLiveData<Int>()
    val renderedFABs: LinkedHashMap<String, FloatingActionButton> = linkedMapOf()

    init {
//        eventsLoaded.postValue(false)
        pos.postValue(0)
    }

}
