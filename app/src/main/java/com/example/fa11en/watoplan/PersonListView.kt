package com.example.fa11en.watoplan

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.RelativeLayout


class PersonListView (val ctx: Context) : RelativeLayout (ctx) {

    init {
        val layoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.person_list_layout, this, true)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        return false
    }

}