package com.chipthink.eternali.watoplan.viewmodels

import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import com.getbase.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable

class SummaryViewState: ViewModel() {

    var sharedPref: SharedPreferences? = null  // shared preferences for theme, etc.

    val containerFrag = Observable<Int>()  // what display fragment is displaying the activities
    val pos = Observable<Int>()  // what activity is in focus
    val menuFabs: LinkedHashMap<String, FloatingActionButton> = linkedMapOf()  // what FABS make up the menu at this point



}
