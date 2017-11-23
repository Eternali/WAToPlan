package com.example.fa11en.watoplan.views.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.R
import com.example.fa11en.watoplan.views.view.TypeListView


class TypeAdapter (val ctx: Context, var resource: Int, var events: MutableList<EventType>)
    : ArrayAdapter<EventType> (ctx, resource, events) {

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            val typeView = TypeListView(ctx)

            // bind variables from layout
            val typeContainer = typeView.findViewById<LinearLayout>(R.id.typeElementContainer)
            val typeName = typeView.findViewById<TextView>(R.id.typeName)

            // set layout to proper values
            typeContainer.setBackgroundResource(events[position].colorNormal)
            typeName.text = events[position].name
            typeName.setBackgroundResource(events[position].colorPressed)

            // click listeners
            typeContainer.setOnClickListener {
                
            }

            return typeView
        }

        return convertView
    }



}