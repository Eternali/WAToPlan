package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fa11en.watoplan.ParameterTypes
import com.example.fa11en.watoplan.R


class EditTypeViewState: ViewModel () {

    val typeName: MutableLiveData<String> = MutableLiveData()
    val typeParams: HashMap<ParameterTypes, MutableLiveData<Boolean>> = hashMapOf()
    val typeColorNormal: MutableLiveData<Int> = MutableLiveData()
    val typeColorPressed: MutableLiveData<Int> = MutableLiveData()

    init {
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