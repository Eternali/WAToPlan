package com.example.fa11en.watoplan

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.fa11en.watoplan.viewmodels.SummaryViewState

import com.example.fa11en.watoplan.views.SummaryView


class DayFragment : Fragment () {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Note: the fragment is guaranteed that the data it needs is loaded
        // (the DayViewModel state is guaranteed)
        if (inflater != null && container != null) {
            val view: View = inflater.inflate(R.layout.day_fragment_layout, null)

            val dayListView: ListView = view.findViewById(R.id.dayView)
            Log.i("DAYEVENTS", SummaryViewState.DayViewModel.getInstance(0).events.value!!.toString())
            dayListView.adapter = EventAdapter(activity, 0,
                    SummaryViewState.DayViewModel.getInstance().events.value!! as MutableList<UserEvent>)

            return view
        }

        return View(activity)
    }

}