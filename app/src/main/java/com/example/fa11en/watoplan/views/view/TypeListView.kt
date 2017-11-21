package com.example.fa11en.watoplan.views.view

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.example.fa11en.watoplan.R


class TypeListView (val ctx: Context): RelativeLayout (ctx) {

    init {
        val layoutInflator = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflator.inflate(R.layout.type_list_layout, this, true)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        return false
    }

}