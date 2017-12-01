package com.example.fa11en.watoplan.views

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.UserEvent
import com.example.fa11en.watoplan.data.dataviewmodel.TypesViewModel
import com.example.fa11en.watoplan.data.dataviewmodel.UserEventsViewModel
import com.example.fa11en.watoplan.viewmodels.SummaryViewState


interface SummaryView {

    // viewmodels
    var state: SummaryViewState
    var events: UserEventsViewModel
    var types: TypesViewModel

    // show database errors
    fun showDbError (ctx: Context, msg: String)

    fun reloadEvents()

    fun reloadTypes()

    // switch view fragment
    fun toggleDisplay (viewid: Int)

    // click on a FAB
    fun addIntent (ctx: Context, typeName: String)

    // click on settings menu item
    fun settingsIntent (ctx: Context)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (ctx: Context)

}