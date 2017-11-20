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

    var state: SummaryViewState
    var appdb: AppDatabase

    // load database
    fun loadDatabase (ctx: Context, state: SummaryViewState): Boolean

    // load types
    fun loadTypes (state: SummaryViewState, db: AppDatabase): Boolean

    // load events
    fun loadEvents (state: SummaryViewState, db: AppDatabase): Boolean

    // switch view fragment
    fun toggleDisplay (view: View)

    // click on an event intent
    fun editIntent (ctx: Context, event: UserEvent)

    // click on a FAB
    fun addIntent (ctx: Context, type: EventType)

    // click on settings menu item
    fun settingsIntent (ctx: Context)

    /*
        Render the view
        @param summaryState: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (summaryState: SummaryViewState, ctx: Context)

}