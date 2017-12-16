package com.chipthink.eternali.watoplan

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*


// TODO: Give this fragment a state

class EventAdapter (val ctx: Context, var resource: Int, var events: MutableList<UserEvent>)
        : ArrayAdapter<UserEvent> (ctx, resource, events) {

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            val eventView = EventListView(ctx)

            // get data from event layout
            val eventContainer = eventView.findViewById<LinearLayout>(R.id.eventElementContainer)
            val time = eventView.findViewById<TextView>(R.id.eventTime)
            val date = eventView.findViewById<TextView>(R.id.eventDate)
            val title = eventView.findViewById<TextView>(R.id.eventTitle)
            val desc = eventView.findViewById<TextView>(R.id.eventDesc)

            // since not all events are shown at once, we can't used the passed position argument
            title.text = events[position].params[ParameterTypes.TITLE] as String
            desc.text = events[position].params[ParameterTypes.DESCRIPTION] as String
            time.text = (events[position].params[ParameterTypes.DATETIME] as Calendar).timestr()
            date.text = (events[position].params[ParameterTypes.DATETIME] as Calendar).datestr()
            eventView.setBackgroundColor(events[position].type!!.colorNormal)
            eventView.background.alpha = (events[position].params[ParameterTypes.PRIORITY] as Int)
                    .toRange(0, 10, 0, 255)

            // onclick listeners
            eventContainer.setOnClickListener {
                val editor = Intent(ctx, EditActivity::class.java)
                editor.putExtra("eid", events[position].eid)
                (ctx as AppCompatActivity).startActivityForResult(editor, RequestCodes.EDITEVENT.code)
            }

            return eventView
        }

        return convertView
    }

}