package com.chipthink.eternali.watoplan.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.chipthink.eternali.watoplan.ParameterTypes
import com.chipthink.eternali.watoplan.data.dataviewmodel.TypesViewModel
import com.chipthink.eternali.watoplan.data.dataviewmodel.UserEventsViewModel
import com.chipthink.eternali.watoplan.viewmodels.EditViewState


interface EditView {

    // viewmodels
    var state: EditViewState
    var types: TypesViewModel
    var events: UserEventsViewModel

    // map each parameter type to a view
    val paramtoView: LinkedHashMap<ParameterTypes, LinearLayout>

    // switch event type
    fun setType(ctx: Context, typeName: String)

    // save event to database
    fun saveEvent (ctx: Context, eid: Int?)

    // show errors from the database
    fun showDbError (ctx: Context, msg: String)

    // end the activity on fatal failure
    fun fail(ctx: Context, msg: String)

    // dialog openers
    fun timeChooser (view: View)

    fun dateChooser (view: View)

    fun mapDialog (view: View)

    /*
        Render the view
        @param state: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (ctx: Context)

}