package com.example.fa11en.watoplan.views

import android.content.Context
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.viewmodels.SettingsViewState


interface SettingsView {

    // status, and database information
    var appdb: AppDatabase

    ////    required intents    ////

    // load database if not done so already.
    // this should check state.dbLoaded and read from bundle
    // or instantiate a new database if necessary.
    fun loadDatabase (ctx: Context, state: SettingsViewState): Boolean

    // show database errors
    fun showDbError (ctx: Context, msg: String)

    // load types if necessary. See loadDatabase notes.
    fun loadTypes (db: AppDatabase, state: SettingsViewState): Boolean

    // make dialog popup to edit event types
    fun editDialog (ctx: Context, state: SettingsViewState)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (state: SettingsViewState, ctx: Context)

}