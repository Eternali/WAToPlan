package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.*


class SummaryViewState: ViewModel () {

//    val eventsLoaded = MutableLiveData<Boolean>()
    val displayFrag = MutableLiveData<Int>()
    val pos = MutableLiveData<Int>()

    init {
//        eventsLoaded.postValue(false)
        pos.postValue(0)
    }

}
