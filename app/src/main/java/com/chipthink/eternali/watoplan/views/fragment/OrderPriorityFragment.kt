package com.chipthink.eternali.watoplan

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class OrderPriorityFragment : Fragment () {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if (inflater != null && container != null) {
            val view: View = inflater.inflate(R.layout.orderpriority_fragment_layout, null)

            return view
        }

        return View(activity)
    }

}
