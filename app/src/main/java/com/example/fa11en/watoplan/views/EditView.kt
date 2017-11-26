package com.example.fa11en.watoplan.views

import android.content.Context
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.viewmodels.EditViewState


interface EditView {

    val appdb: AppDatabase

    // save event to database
    fun saveEvent (state: EditViewState): Boolean

    // show errors from the database
    fun showDbError (ctx: Context, msg: String)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (state: EditViewState, ctx: Context)

}