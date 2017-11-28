package com.example.fa11en.watoplan.views

import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.UserEvent
import com.example.fa11en.watoplan.viewmodels.SummaryViewState


interface SummaryView {

    var appdb: AppDatabase

    // load database
    fun loadDatabase (ctx: Context): Boolean

    // show database errors
    fun showDbError (ctx: Context, msg: String)

    // load types
    fun loadTypes (state: SummaryViewState): Boolean

    // load events
    fun loadEvents (state: SummaryViewState): Boolean

    // switch view fragment
    fun toggleDisplay (viewid: Int)

    // click on an event intent
    fun editIntent (ctx: Context, eid: Int)

    // click on a FAB
    fun addIntent (ctx: Context, typeName: String)

    // click on settings menu item
    fun settingsIntent (ctx: Context)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (state: SummaryViewState, ctx: Context)

}