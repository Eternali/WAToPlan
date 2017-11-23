package com.example.fa11en.watoplan.views

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.viewmodels.EditTypeViewState


interface EditTypeView {

    val appdb: AppDatabase

    ////    required intents    ////

    // save the event type to database
    fun saveType (state: EditTypeViewState): Boolean

    // show errors from the database
    fun showDbError (ctx: Context, msg: String)

    // show color dialog
    fun showColorChooser (ctx: Context, colorData: MutableLiveData<Int>)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (state: EditTypeViewState, ctx: Context)

}