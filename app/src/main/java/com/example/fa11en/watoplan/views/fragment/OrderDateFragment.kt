package com.example.fa11en.watoplan

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.fa11en.watoplan.data.dataviewmodel.UserEventsViewModel


class OrderDateFragment : Fragment () {

    lateinit var events: UserEventsViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Note: the fragment is guaranteed that the data it needs is loaded
        // (the DayViewModel state is guaranteed)
        if (inflater != null && container != null) {
            events = ViewModelProviders.of(activity as AppCompatActivity).get(UserEventsViewModel::class.java)

            val view: View = inflater.inflate(R.layout.orderdate_fragment_layout, null)
            val byDateListView: ListView = view.findViewById(R.id.dayView)

            val byDateAdapter = EventAdapter(activity, 0,
                    events.value!!.value!!.toMutableList())
            byDateListView.adapter = byDateAdapter

            val eventsObserver: Observer<List<UserEvent>> = Observer {
                Log.i("EVENT", it?.get(0)?.type.toString())
                byDateAdapter.notifyDataSetChanged()
            }
            events.value!!.observe(activity as AppCompatActivity, eventsObserver)

            return view
        }

        return View(activity)
    }

}