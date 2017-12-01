package com.example.fa11en.watoplan.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.Person
import java.util.*


class EditViewState : ViewModel () {

    val curType: MutableLiveData<EventType> = MutableLiveData()

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

    init {
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