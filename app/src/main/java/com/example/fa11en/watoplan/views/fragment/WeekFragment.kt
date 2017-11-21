package com.example.fa11en.watoplan

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class WeekFragment : Fragment () {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if (inflater != null && container != null
                && (activity as MainActivity).state.eventsLoaded.value != null
                && (activity as MainActivity).state.eventsLoaded.value!!) {
            val view: View = inflater.inflate(R.layout.week_fragment_layout, null)

            return view
        }

        return View(activity)
    }

}
