package com.example.fa11en.watoplan

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.fa11en.watoplan.viewmodels.SummaryViewState


class OrderDateFragment : Fragment () {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Note: the fragment is guaranteed that the data it needs is loaded
        // (the DayViewModel state is guaranteed)
        if (inflater != null && container != null) {
            val view: View = inflater.inflate(R.layout.orderdate_fragment_layout, null)
            val byDateListView: ListView = view.findViewById(R.id.dayView)

            val byDateAdapter = EventAdapter(activity, 0,
                    SummaryViewState.events.value!!.toMutableList())
            byDateListView.adapter = byDateAdapter

            val eventsObserver: Observer<List<UserEvent>> = Observer {
                byDateAdapter.notifyDataSetChanged()
            }
            SummaryViewState.events.observe(activity as AppCompatActivity, eventsObserver)

            return view
        }

        return View(activity)
    }

}