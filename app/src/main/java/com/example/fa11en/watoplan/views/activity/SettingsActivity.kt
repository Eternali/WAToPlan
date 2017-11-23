package com.example.fa11en.watoplan

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.fa11en.watoplan.viewmodels.SettingsViewState
import com.example.fa11en.watoplan.views.SettingsView
import com.example.fa11en.watoplan.views.adapter.TypeAdapter
import com.example.fa11en.watoplan.views.dialog.EditTypeActivity
import kotterknife.bindView


// global note: it == true is shorthand for it != null && it

class SettingsActivity : AppCompatActivity (), SettingsView {

    // use kotterknife to bind views to vals

    // theme switch
    private val themeSwitch: Switch by bindView(R.id.themeSwitch)

    // event types
    private val eventContainer: LinearLayout by bindView(R.id.eventTypesContainer)
    private val eventList: ListView by bindView(R.id.eventsEditList)

    lateinit override var appdb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        // set content view to layout
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // initialize state to loading (neither the database or events are loaded yet)
        render(SettingsViewState.Loading(Themes.LIGHT, false, false), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodes.NEWEVENTTYPE.code -> {
                if (resultCode == ResultCodes.TYPESAVED.code)
                    // notify adapter of type changed
                    {}
            }
        }
    }


    ////**** INTENTS ****////

    override fun loadDatabase(ctx: Context): Boolean {
        return try {
            appdb = EventsDB.getInstance(ctx)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    // TODO: make a database intents class for application-wide data loading
    override fun loadTypes(db: AppDatabase, state: SettingsViewState.Loading): Boolean {
        db.beginTransaction()
        return try {
            val type = EventType("TestType", mutableListOf(ParameterTypes.TITLE, ParameterTypes.DESCRIPTION),
                    R.color.colorAccent, R.color.colorAccent_pressed)
            db.typeDao().insert(type)
            state.types.postValue(db.typeDao().getAll())
            true
        } catch (e: Exception) {
            false
        } finally {
            db.endTransaction()
        }
    }

    override fun editDialog(ctx: Context, state: SettingsViewState.Passive) {
        startActivityForResult(Intent(ctx, EditTypeActivity::class.java), RequestCodes.NEWEVENTTYPE.code)
    }

    override fun render(state: SettingsViewState, ctx: Context) {
        when (state) {
            is SettingsViewState.Loading -> {
                // watch loading status
                val dbLoadingObserver: Observer<Boolean> = Observer {
                    if (it == true) {
                        loadTypes(appdb, state)
                    }
                }
                val typesLoadingObserver: Observer<List<EventType>> = Observer {
                    if (it == null || it.isEmpty()) {
                        showDbError(ctx, "Failed to load Event Types")
                    } else {
                        state.typesLoaded.postValue(true)
                    }
                }
                val finishedLoadingObserver: Observer<Boolean> = Observer {
                    if (state.typesLoaded.value == true) {
                        // if typesLoaded is true then types is guaranteed to be defined.
                        render(SettingsViewState.Passive(state.theme.value!!, state.types.value!!), ctx)
                    }
                }
                state.dbLoaded.observe(this, dbLoadingObserver)
                state.types.observe(this, typesLoadingObserver)
                state.typesLoaded.observe(this, finishedLoadingObserver)

                // load database
                if (loadDatabase(ctx)) state.dbLoaded.postValue(true)
                else showDbError(ctx, "Failed to Load Database")
            }
            is SettingsViewState.Passive -> {
                // now that everything is loaded, set listview adapter
                eventList.adapter = TypeAdapter(this, 0, state.types)
            }
        }

    }

}
