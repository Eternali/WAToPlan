package com.example.fa11en.watoplan.views

import android.content.Context
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.Themes
import com.example.fa11en.watoplan.data.dataviewmodel.TypesViewModel
import com.example.fa11en.watoplan.viewmodels.SettingsViewState


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

    // set the type
    fun setThemePref (ctx: Context, theme: Themes?)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (ctx: Context)

}