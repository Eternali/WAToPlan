package com.chipthink.eternali.watoplan.views

import android.content.Context
import com.chipthink.eternali.watoplan.EventType
import com.chipthink.eternali.watoplan.Themes
import com.chipthink.eternali.watoplan.data.dataviewmodel.TypesViewModel
import com.chipthink.eternali.watoplan.viewmodels.SettingsViewState


interface SettingsView {

    // viewmodels
    var state: SettingsViewState
    var types: TypesViewModel


    ////    required intents    ////

    fun reloadTypes (ctx: Context)

    // show database errors
    fun showDbError (ctx: Context, msg: String)

    // make dialog popup to edit event types
    fun editDialog (ctx: Context, eventType: EventType?)

    // save theme to shared preferences
    fun setThemePref (ctx: Context, theme: Themes)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (ctx: Context)

}