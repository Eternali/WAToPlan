package com.example.fa11en.watoplan.views

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.example.fa11en.watoplan.AppDatabase
import com.example.fa11en.watoplan.data.dataviewmodel.TypesViewModel
import com.example.fa11en.watoplan.viewmodels.EditTypeViewState


interface EditTypeView {

    var state: EditTypeViewState
    var types: TypesViewModel

    ////    required intents    ////

    fun fail (ctx: Context, msg: String)

    fun saveType (ctx: Context)

    fun deleteType (ctx: Context)

    // show errors from the database
    fun showDbError (ctx: Context, msg: String)

    // show color dialog
    fun showColorChooser (ctx: Context, colorData: MutableLiveData<Int>)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (ctx: Context)

}