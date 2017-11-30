package com.example.fa11en.watoplan.views.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fa11en.watoplan.EventType
import com.example.fa11en.watoplan.R
import com.example.fa11en.watoplan.SettingsActivity
import com.example.fa11en.watoplan.viewmodels.SettingsViewState
import com.example.fa11en.watoplan.views.SettingsView
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
            val backgroundDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(events[position].colorPressed, events[position].colorNormal))
            backgroundDrawable.cornerRadius = 4f
            typeContainer.setBackgroundDrawable()
            typeName.text = events[position].name
            typeName.setBackgroundColor(events[position].colorPressed)

            // click listeners
            typeContainer.setOnClickListener {
                (ctx as SettingsActivity).editDialog(ctx, events[position])
            }

            return typeView
        }

        return convertView
    }



}