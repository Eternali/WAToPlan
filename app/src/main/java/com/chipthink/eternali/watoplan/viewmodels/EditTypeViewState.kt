package com.chipthink.eternali.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.chipthink.eternali.watoplan.ParameterTypes
import com.chipthink.eternali.watoplan.R


class EditTypeViewState: ViewModel () {

    val typeName: MutableLiveData<String> = MutableLiveData()
    val typeParams: LinkedHashMap<ParameterTypes, MutableLiveData<Boolean>> = linkedMapOf()
    val typeColorNormal: MutableLiveData<Int> = MutableLiveData()
    val typeColorPressed: MutableLiveData<Int> = MutableLiveData()

    val isEdit: MutableLiveData<Boolean> = MutableLiveData()

    init {
        // initialize all parameters to false
        ParameterTypes.values().forEach {
            typeParams[it] = MutableLiveData()
            typeParams[it]!!.postValue(false)  // WATCH NULL-SAFE ASSERTION
        }
        // set defaults
        typeParams[ParameterTypes.TITLE]?.postValue(true)
        typeParams[ParameterTypes.DESCRIPTION]?.postValue(true)
        typeParams[ParameterTypes.NOTIS]?.postValue(true)

        // initialize other event type parameters
        typeName.postValue("")
        typeColorNormal.postValue(R.color.colorAccent)
        typeColorPressed.postValue(R.color.colorAccent_pressed)
    }

}