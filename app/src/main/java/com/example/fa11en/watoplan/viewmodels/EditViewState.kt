package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.ParameterTypes


class EditViewState: ViewModel () {

    val types: List<EventType> = listOf()
    val type: MutableLiveData<EventType> = MutableLiveData()
    val params: LinkedHashMap<ParameterTypes, Any> = linkedMapOf()

    init {

    }

}