package com.example.fa11en.watoplan

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.example.fa11en.watoplan.views.SummaryView


class DayFragment : Fragment () {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if (inflater != null && container != null
                && (activity as MainActivity).state.eventsLoaded.value != null
                && (activity as MainActivity).state.eventsLoaded.value!!) {
            val view: View = inflater.inflate(R.layout.day_fragment_layout, null)

            val dayListView: ListView = view.findViewById(R.id.dayView)
            dayListView.adapter = EventAdapter(activity, 0, (activity as SummaryView).state.events.value!!)

            return view
        }

        return View(activity)
    }

}