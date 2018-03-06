package com.chipthink.eternali.watoplan.views.activities

import android.app.Activity
import android.os.Bundle
import com.chipthink.eternali.watoplan.R
import com.chipthink.eternali.watoplan.views.SummaryView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), SummaryView {

    override fun onCreate(savedInstanceState: Bundle?) {

        // must get state and set theme before super called


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

}