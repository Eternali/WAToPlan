package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.ParameterTypes
import com.example.fa11en.watoplan.R


sealed class EditTypeViewState: ViewModel () {

    val typeName: MutableLiveData<String> = MutableLiveData()
    val typeParams: LinkedHashMap<ParameterTypes, MutableLiveData<Boolean>> = linkedMapOf()
    val typeColorNormal: MutableLiveData<Int> = MutableLiveData()
    val typeColorPressed: MutableLiveData<Int> = MutableLiveData()

    class New: EditTypeViewState () {

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

    class Edit private constructor (type: EventType): EditTypeViewState () {

        init {
            // initialize all values to those of the type

            ParameterTypes.values().forEach {
                typeParams[it] = MutableLiveData()
                if (it in type.parameters) typeParams[it]!!.postValue(true)
                else typeParams[it]!!.postValue(false)
            }

            typeName.postValue(type.name)
            typeColorNormal.postValue(type.colorNormal)
            typeColorPressed.postValue(type.colorPressed)
        }

        companion object {
            private var INSTANCE: Edit? = null
            // only specify type if reinstantiating
            fun getInstance (type: EventType?): EditTypeViewState {
                if (type != null)
                    INSTANCE = Edit(type)
                else if (INSTANCE == null)
                    return EditTypeViewState.New()

                return INSTANCE!!
            }
            fun isInstantiated (): Boolean = INSTANCE != null
            fun destroyInstance () {
                INSTANCE = null
            }
        }

    }

}