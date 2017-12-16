package com.chipthink.eternali.watoplan.views.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.chipthink.eternali.watoplan.EventType
import com.chipthink.eternali.watoplan.R
import com.chipthink.eternali.watoplan.SettingsActivity
import com.chipthink.eternali.watoplan.views.view.TypeListView


class TypeAdapter (val ctx: Context, var resource: Int, var types: MutableList<EventType>)
    : ArrayAdapter<EventType> (ctx, resource, types) {

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
            // create radial background gradient (orientation is ignored but required for constructor)
            val backgroundDrawable = GradientDrawable(GradientDrawable.Orientation.TL_BR,
                    intArrayOf(types[position].colorPressed, types[position].colorNormal))
            backgroundDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
            backgroundDrawable.gradientRadius = 200f
            backgroundDrawable.cornerRadius = 8f
            typeContainer.background = backgroundDrawable
            typeName.text = types[position].name

            // click listeners
            typeContainer.setOnClickListener {
                (ctx as SettingsActivity).editDialog(ctx, types[position])
            }

            return typeView
        }

        return convertView
    }



}