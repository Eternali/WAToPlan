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

    // parameters
    // NOTE: I HAD THIS AS A LINKED HASHMAP, BUT SINCE ANDROID COMPLAINED THAT IT CAN'T POST TO
    // MutableLiveData<*> (out projected type) I can't do that
//    val params: LinkedHashMap<ParameterTypes, *> = linkedMapOf(
//            ParameterTypes.TITLE to "",
//            ParameterTypes.DESCRIPTION to "",
//            ParameterTypes.DATETIME to Calendar.getInstance(),
//            ParameterTypes.LOCATION to Location("gps"),
//            ParameterTypes.ENTITIES to mutableListOf<Person>(),
//            ParameterTypes.REPEAT to mutableListOf<Calendar>()
//    )

    val name = MutableLiveData<String>()
    val desc = MutableLiveData<String>()
    val datetime = MutableLiveData<Calendar>()
    val notis = MutableLiveData<MutableList<Int>>()
    val location = MutableLiveData<Location>()
    val entities = MutableLiveData<MutableList<Person>>()
    val repetitions = MutableLiveData<MutableList<Calendar>>()
    val progress = MutableLiveData<Int>()
    val priority = MutableLiveData<Int>()

    val isEdit: MutableLiveData<Boolean> = MutableLiveData()
    val loaded: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loaded.postValue(false)
        name.postValue("")
        desc.postValue("")
        notis.postValue(mutableListOf())
        datetime.postValue(Calendar.getInstance())
        location.postValue(Location("gps"))
        entities.postValue(mutableListOf())
        repetitions.postValue(mutableListOf())
        progress.postValue(0)
        priority.postValue(0)
    }

}