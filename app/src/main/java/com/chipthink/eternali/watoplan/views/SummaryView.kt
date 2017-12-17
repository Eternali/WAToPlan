package com.chipthink.eternali.watoplan.views

import android.content.Context
import com.chipthink.eternali.watoplan.data.dataviewmodel.TypesViewModel
import com.chipthink.eternali.watoplan.data.dataviewmodel.UserEventsViewModel
import com.chipthink.eternali.watoplan.viewmodels.SummaryViewState


interface SummaryView {

    // viewmodels
    var state: SummaryViewState
    var events: UserEventsViewModel
    var types: TypesViewModel

    // set theme and recreate activity
    fun setTheme (ctx: Context)

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