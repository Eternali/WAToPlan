package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.ParameterTypes
import com.example.fa11en.watoplan.Person
import java.util.*


class EditViewState : ViewModel () {

    var types: List<EventType> = listOf()
    val curType: MutableLiveData<EventType> = MutableLiveData()
    val params: MutableLiveData<LinkedHashMap<ParameterTypes, MutableLiveData<*>>> = MutableLiveData()
    val isEdit: MutableLiveData<Boolean> = MutableLiveData()
    val loaded: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loaded.postValue(false)
        params.postValue(linkedMapOf())
    }

    companion object {
        fun initializeParam (param: ParameterTypes): Any = when (param) {
            ParameterTypes.TITLE -> ""
            ParameterTypes.DESCRIPTION -> ""
            ParameterTypes.DATETIME -> Calendar.getInstance()
            ParameterTypes.LOCATION -> Location("gps")
            ParameterTypes.ENTITIES -> mutableListOf<Person>()
            ParameterTypes.REPEAT -> mutableListOf<Calendar>()
        }

        fun initializeParam (param: ParameterTypes, live: Boolean): MutableLiveData<*> = when (param) {
            ParameterTypes.TITLE -> {
                val data = MutableLiveData<String>()
                data.postValue("")
                data
            }
            ParameterTypes.DESCRIPTION -> {
                val data = MutableLiveData<String>()
                data.postValue("")
                data
            }
            ParameterTypes.DATETIME -> {
                val data = MutableLiveData<Calendar>()
                data.postValue(Calendar.getInstance())
                data
            }
            ParameterTypes.LOCATION -> {
                val data = MutableLiveData<Location>()
                data.postValue(Location("gps"))
                data
            }
            ParameterTypes.ENTITIES -> {
                val data: MutableLiveData<MutableList<Person>> = MutableLiveData()
                data.postValue(mutableListOf())
                data
            }
            ParameterTypes.REPEAT -> {
                val data: MutableLiveData<MutableList<Calendar>> = MutableLiveData()
                data.postValue(mutableListOf())
                data
            }
        }
    }

}