package com.chipthink.eternali.watoplan

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.widget.*
import com.chipthink.eternali.watoplan.data.dataviewmodel.TypesViewModel
import com.chipthink.eternali.watoplan.viewmodels.SettingsViewState
import com.chipthink.eternali.watoplan.views.SettingsView
import com.chipthink.eternali.watoplan.views.adapter.TypeAdapter
import com.chipthink.eternali.watoplan.views.dialog.EditTypeActivity
import kotterknife.bindView


// global note: it == true is shorthand for it != null && it

class SettingsActivity : AppCompatActivity (), SettingsView {

    // viewmodels
    lateinit override var state: SettingsViewState
    lateinit override var types: TypesViewModel

    // use kotterknife to bind views to vals

    // theme switch
    private val themeSwitch: Switch by bindView(R.id.themeSwitch)

    // event types
    private val eventContainer: LinearLayout by bindView(R.id.eventTypesContainer)
    private val typeList: ListView by bindView(R.id.typesEditList)
    private val addTypeButton: Button by bindView(R.id.addTypeButton)


    override fun onCreate(savedInstanceState: Bundle?) {

        // must set theme before super is called
        state = ViewModelProviders.of(this).get(SettingsViewState::class.java)
        state.sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setTheme(this, sharedPref = state.sharedPref)

        // set content view to layout
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        types = ViewModelProviders.of(this).get(TypesViewModel::class.java)
        render(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodes.NEWEVENTTYPE.code -> {
                if (resultCode == ResultCodes.TYPESAVED.code) {
                    reloadTypes(applicationContext)
                } else if (resultCode == ResultCodes.TYPEFAILED.code) {
                    showDbError(applicationContext, "Failed to Save Event.")
                }
            }
            RequestCodes.EDITEVENTTYPE.code -> {
                if (resultCode == ResultCodes.TYPEDELETED.code) {
                    reloadTypes(applicationContext)
                } else if (resultCode == ResultCodes.TYPECHANGED.code) {
                    reloadTypes(applicationContext)
                } else if (resultCode == ResultCodes.TYPEFAILED.code) {
                    showDbError(applicationContext, "Failed to Save Event.")
                }
            }
        }
    }

    override fun onBackPressed() {
        val code = Intent()
        setResult(ResultCodes.TYPECHANGED.code, code)
        finish()
        super.onBackPressed()
    }


    ////**** INTENTS ****////

    override fun reloadTypes(ctx: Context) {

    }

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    override fun editDialog(ctx: Context, eventType: EventType?) {
        if (eventType != null) {
            val editIntent = Intent(ctx, EditTypeActivity::class.java)
            editIntent.putExtra("typename", eventType.name)
            startActivityForResult(editIntent, RequestCodes.EDITEVENTTYPE.code)
        } else
            startActivityForResult(Intent(ctx, EditTypeActivity::class.java), RequestCodes.NEWEVENTTYPE.code)
    }

    override fun setThemePref(ctx: Context, theme: Themes) {
        if (state.sharedPref == null) {
            showDbError(ctx, "Failed to load preferences.")
            finish()
        }

        val curTheme = state.sharedPref?.getString("theme", Themes.LIGHT.name)
        Log.i("THEME CURRENTLY APPLIED", curTheme)

        // only open editor if we need to change it to avoid pref change listeners being called
        if (curTheme != theme.name) {
            val spEditor: SharedPreferences.Editor = state.sharedPref!!.edit()
            spEditor.putString("theme", theme.name)
            spEditor.apply()
        }
    }

    override fun render(ctx: Context) {

        // initialize observers
        state.prefListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPref, key -> run {
            when (key) {
                "theme" -> setTheme(this, sharedPref = sharedPref)
            }
            recreate()
        } }
        val typesObserver: Observer<List<EventType>> = Observer {
            if (it != null)
                typeList.adapter = TypeAdapter(this, 0, it.toMutableList())
        }

        state.sharedPref?.registerOnSharedPreferenceChangeListener(state.prefListener)
        types.value?.observe(this, typesObserver)

        themeSwitch.setOnClickListener {
            if ((it as Switch).isChecked) {
                Log.i("THEME REQUESTED", Themes.DARK.name)
                setThemePref(this, Themes.DARK)
            } else {
                Log.i("THEME REQUESTED", Themes.LIGHT.name)
                setThemePref(this, Themes.LIGHT)
            }
        }

        addTypeButton.setOnClickListener { editDialog(ctx, null) }
    }

}
