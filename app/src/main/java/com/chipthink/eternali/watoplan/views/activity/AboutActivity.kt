package com.chipthink.eternali.watoplan.views.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class AboutActivity : AppCompatActivity (), AboutView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        render(this)
    }

}