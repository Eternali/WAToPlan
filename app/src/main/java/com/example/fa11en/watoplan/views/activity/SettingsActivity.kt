package com.example.fa11en.watoplan

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import com.example.fa11en.watoplan.data.dataviewmodel.TypesViewModel
import com.example.fa11en.watoplan.viewmodels.EditTypeViewState
import com.example.fa11en.watoplan.viewmodels.SettingsViewState
import com.example.fa11en.watoplan.views.SettingsView
import com.example.fa11en.watoplan.views.adapter.TypeAdapter
import com.example.fa11en.watoplan.views.dialog.EditTypeActivity
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

        // set content view to layout
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        state = ViewModelProviders.of(this).get(SettingsViewState::class.java)
        types = ViewModelProviders.of(this).get(TypesViewModel::class.java)
        state.sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)

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

    override fun setThemePref(ctx: Context, theme: Themes?) {
        if (state.sharedPref == null || theme == null) showDbError(ctx, "Failed to change theme")
        val spEditor: SharedPreferences.Editor = state.sharedPref!!.edit()

        if (theme != null) spEditor.putString("theme", theme.name)

        val curTheme = TypedValue()
        ctx.theme.resolveAttribute(R.attr.theme_name, curTheme, true)
        val theme = state.sharedPref?.getString("theme", Themes.LIGHT.name)

        if (curTheme.string != null) {
            when (theme) {
                Themes.LIGHT.name -> ctx.setTheme(R.style.AppThemeLight)
                Themes.DARK.name -> ctx.setTheme(R.style.AppThemeDark)
            }
        }

        spEditor.apply()
        recreate()
    }

    override fun render(ctx: Context) {

        // initialize observers
        val typesObserver: Observer<List<EventType>> = Observer {
            if (it != null)
                typeList.adapter = TypeAdapter(this, 0, it.toMutableList())
        }
        val themeObserver: Observer<Themes> = Observer {
            setThemePref(ctx, it)
        }

        types.value?.observe(this, typesObserver)
        state.theme.observe(this, themeObserver)

        themeSwitch.setOnClickListener {
            if (it.isActivated) state.theme.postValue(Themes.DARK)
            else state.theme.postValue(Themes.LIGHT)
        }

        addTypeButton.setOnClickListener { editDialog(ctx, null) }
    }

}
