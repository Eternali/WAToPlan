package com.example.fa11en.watoplan.views

import android.content.Context
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.UserEvent


interface SummaryView {

    // click on an event intent
    fun settingsIntent (ctx: Context, type: EventType)

    // click on a FAB
    fun addIntent (ctx: Context, type: EventType): EventType

    /*
        Render the view
        @param summaryState: the viewState to be displayed
        @param ctx: the context to be rendered in
    */
    fun render (summaryState: SummaryViewState, ctx: Context)

}