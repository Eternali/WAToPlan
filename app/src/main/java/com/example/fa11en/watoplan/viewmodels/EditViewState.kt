package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.ParameterTypes
import com.example.fa11en.watoplan.Person
import java.util.*
import kotlin.collections.LinkedHashMap


class EditViewState : ViewModel () {

    var types: List<EventType> = listOf()
    val curType: MutableLiveData<EventType> = MutableLiveData()
    val params: MutableLiveData<LinkedHashMap<ParameterTypes, MutableLiveData<Any>>> = MutableLiveData()
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

        fun initializeParam (paramMap: LinkedHashMap<ParameterTypes, MutableLiveData<Any>>, param: ParameterTypes)
                = when (param) {
            ParameterTypes.TITLE -> {
                val data = MutableLiveData<Any>()
                data.postValue("")
                paramMap.put(param, data)
            }
            ParameterTypes.DESCRIPTION -> {
                val data = MutableLiveData<Any>()
                data.postValue("")
                paramMap.put(param, data)
            }
            ParameterTypes.DATETIME -> {
                val data = MutableLiveData<Any>()
                data.postValue(Calendar.getInstance())
                paramMap.put(param, data)
            }
            ParameterTypes.LOCATION -> {
                val data = MutableLiveData<Any>()
                data.postValue(Location("gps"))
                paramMap.put(param, data)
            }
            ParameterTypes.ENTITIES -> {
                val data: MutableLiveData<Any> = MutableLiveData()
                val initList: MutableList<Person> = mutableListOf()
                data.postValue(initList)
                paramMap.put(param, data)
            }
            ParameterTypes.REPEAT -> {
                val data: MutableLiveData<Any> = MutableLiveData()
                val initList: MutableList<Date> = mutableListOf()
                data.postValue(initList)
                paramMap.put(param, data)
            }
        }
    }

}