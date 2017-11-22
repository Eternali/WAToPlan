package com.example.fa11en.watoplan

import android.app.FragmentTransaction
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.ToggleButton
import com.example.fa11en.watoplan.viewmodels.SummaryViewState
import com.example.fa11en.watoplan.views.SummaryView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import kotlinx.android.synthetic.main.activity_main.view.*
import kotterknife.bindView


/*
    Models:
        - mainViewState: eventTypes, userEvents, pos, displayType
        - editViewState: parameterTypes, userEvent
        -
 */


// global enum of themes
enum class Themes {
    DARK,
    LIGHT
}

// global enum for request codes
enum class RequestCodes (val code: Int) {
    NEWEVENTTYPE(0)
}

// global enum for result codes
enum class ResultCodes (val code: Int) {
    TYPECANCELED(0),
    TYPESAVED(1),
    TYPEFAILED(2)
}

// This can be put here because it will never change and is required globally
enum class ParameterTypes (val param: String) {
    TITLE ("TITLE"),
    DESCRIPTION ("DESCRIPTION"),
    DATETIME ("DATETIME"),
    LOCATION ("LOCATION"),
    ENTITIES ("ENTITIES"),
    REPEAT ("REPEAT")
}


class MainActivity: AppCompatActivity (), SummaryView {

    // use kotterknife to bind views to vals

    // FAM
    private val addMenu: FloatingActionsMenu by bindView(R.id.addMenu)

    // view selection
    private val displayGroup: RadioGroup by bindView(R.id.overviewLayoutSwitcher)
    private val dayToggle: ToggleButton by bindView(R.id.dayToggle)
    private val weekToggle: ToggleButton by bindView(R.id.weekToggle)
    private val monthToggle: ToggleButton by bindView(R.id.monthToggle)

    lateinit private var dotMenu: Menu

    // activity state variables
    lateinit override var appdb: AppDatabase

    override fun onCreate (savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        // set content view to layout
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        render(SummaryViewState.Loading(false, false, false, dayToggle.id),this)
    }

    override fun onCreateOptionsMenu (menu: Menu?): Boolean {
        if (menu == null) return false
        menuInflater.inflate(R.menu.dot_menu, menu)
        dotMenu = menu
        return true
    }

    override fun onOptionsItemSelected (item: MenuItem?): Boolean {
        if (item == null) return false
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return true
    }

    override fun onKeyDown (keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            dotMenu.performIdentifierAction(R.id.dot_menu, 0)
            return true
        }

        return super.onKeyDown(keyCode, event)
    }


    ////**** INTENTS ****////

    override fun loadDatabase (ctx: Context): Boolean {
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

    override fun loadTypes (state: SummaryViewState, db: AppDatabase): Boolean {
        db.beginTransaction()
        return try {
            state.types.postValue(db.typeDao().getAll().toMutableList())
            true
        } catch (e: Exception) {
            false
        } finally {
            db.endTransaction()
        }
    }

    override fun loadEvents (state: SummaryViewState, db: AppDatabase): Boolean {
        db.beginTransaction()
        return try {
            // alter state to reflect success
            state.events.postValue(db.eventDao().getAll().toMutableList())
            true
        } catch (e: Exception) {
            false
        } finally {
            db.endTransaction()
        }
    }

    override fun toggleDisplay (view: View) {
        displayGroup.clearCheck()
        displayGroup.check(view.id)

        render(SummaryViewState.Loading(true, true, true, view.id), this)
    }

    override fun editIntent (ctx: Context, event: UserEvent) {
        startActivity(Intent(ctx, EditActivity::class.java))
    }

    override fun addIntent (ctx: Context, type: EventType) {
        startActivity(Intent(ctx, EditActivity::class.java))
    }

    override fun settingsIntent (ctx: Context) {
        startActivity(Intent(ctx, SettingsActivity::class.java))
    }

    override fun render (state: SummaryViewState, ctx: Context) {

        // fragment handling
        when (state) {
            is SummaryViewState.Loading -> {
                // watch loading status
                val dbLoadingObserver: Observer<Boolean> = Observer {
                    if (it != null && it) {
                        // LOOK OUT FOR NULL-SAFETY PROMISE
                        if (loadTypes(state, appdb)) state.typesLoaded.postValue(true)
                        else showDbError(ctx, "Failed to load Event Types.")
                        if (loadEvents(state, appdb)) state.eventsLoaded.postValue(true)
                        else showDbError(ctx, "Failed to load Events.")
                    }
                }

                state.dbLoaded.observe(this, dbLoadingObserver)

                // load database if not already done so
                if (state.dbLoaded.value == null || !state.dbLoaded.value!!) {
                    if (loadDatabase(ctx)) state.dbLoaded.postValue(true)
                    else showDbError(ctx, "Failed to load Database.")
                }

                // set display fragment
                // TODO: only replace fragment when previous is different from requested
                val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                when (state.displayFrag.value) {
                    R.id.dayToggle -> {
                        fragTransaction.replace(R.id.displayFragContainer, DayFragment(), "day")
                        fragTransaction.commit()
                        render(SummaryViewState.DayViewModel(0), this)
                    }
                    R.id.weekToggle -> {
                        fragTransaction.replace(R.id.displayFragContainer, WeekFragment(), "day")
                        fragTransaction.commit()
                        render(SummaryViewState.WeekViewModel(), this)
                    }
                    R.id.monthToggle -> {
                        fragTransaction.replace(R.id.displayFragContainer, MonthFragment(), "day")
                        fragTransaction.commit()
                        render(SummaryViewState.MonthViewModel(), this)
                    }
                    else -> fragTransaction.commit()
                }
            }
            is SummaryViewState.DayViewModel -> {}
            is SummaryViewState.WeekViewModel -> {}
            is SummaryViewState.MonthViewModel -> {}
        }

        // make radio group from togglers
        displayGroup.setOnCheckedChangeListener({group, checkedId ->
            for (t in 0..group.childCount) {
                if (group.getChildAt(t) == null) continue
                val view: ToggleButton = group.getChildAt(t) as ToggleButton
                view.isChecked = view.id == checkedId
            }
        })

        // generate FABS
        state.types.value?.forEach {
            val button = FloatingActionButton(ctx)
            button.size = FloatingActionButton.SIZE_MINI
            button.setColorNormalResId(it.colorNormal)
            button.setColorPressedResId(it.colorPressed)
            button.title = it.name
            button.setOnClickListener { _ ->
                addMenu.collapse()
                addIntent(ctx, it)
            }
            addMenu.addButton(button)
        }

    }

}
