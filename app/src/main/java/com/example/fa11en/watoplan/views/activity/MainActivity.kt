package com.example.fa11en.watoplan

import android.app.FragmentTransaction
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
    private val displayGroup: RadioGroup by bindView(R.id.displayToggleGroup)
    private val dayToggle: ToggleButton by bindView(R.id.dayToggle)
    private val weekToggle: ToggleButton by bindView(R.id.weekToggle)
    private val monthToggle: ToggleButton by bindView(R.id.monthToggle)

    lateinit private var dotMenu: Menu

    lateinit override var state: SummaryViewState
    lateinit override var appdb: AppDatabase

    override fun onCreate (savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        // set content view to layout
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        state = SummaryViewState.DayViewModel(0, false, false, false)
        render(state,this)
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

    override fun loadDatabase (ctx: Context, state: SummaryViewState): Boolean {
        return try {
            appdb = EventsDB.getInstance(ctx)
            state.dbLoaded.postValue(true)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun loadTypes (state: SummaryViewState, db: AppDatabase): Boolean {
        db.beginTransaction()
        return try {
            state.types.postValue(db.typeDao().getAll().toMutableList())
            state.typesLoaded.postValue(true)
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
            state.eventsLoaded.postValue(true)
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

        val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (view.id) {
            R.id.dayToggle -> {
                state = SummaryViewState.DayViewModel(0, true, true,true)
                fragTransaction.replace(R.id.displayFragContainer, DayFragment(), "day")
                render(state, this)
            }
            R.id.weekToggle -> {
                state = SummaryViewState.WeekViewModel()
                fragTransaction.replace(R.id.displayFragContainer, WeekFragment(), "day")
                render(state, this)
            }
            R.id.monthToggle -> {
                state = SummaryViewState.MonthViewModel()
                fragTransaction.replace(R.id.displayFragContainer, MonthFragment(), "day")
                render(state, this)
            }
        }
        fragTransaction.commit()
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

    override fun render (summaryState: SummaryViewState, ctx: Context) {

        // fragment handling
        when (state) {
            is SummaryViewState.DayViewModel -> {}
            is SummaryViewState.WeekViewModel -> {}
            is SummaryViewState.MonthViewModel -> {}
        }

        // load database if not already done so
        if (summaryState.dbLoaded.value == null || !(summaryState.dbLoaded.value as Boolean))
            loadDatabase(ctx, summaryState)

        // load event types and events if not already done so
        if (summaryState.typesLoaded.value == null || !(summaryState.typesLoaded.value as Boolean))
            loadTypes(summaryState, appdb)
        if (summaryState.eventsLoaded.value == null || !(summaryState.eventsLoaded.value as Boolean))
            loadEvents(summaryState, appdb)

        // make radio group from togglers
        displayGroup.setOnCheckedChangeListener({group, checkedId ->
            for (t in 0..group.childCount) {
                if (group.getChildAt(t) == null) continue
                val view: ToggleButton = group.getChildAt(t) as ToggleButton
                view.isChecked = view.id == checkedId
            }
        })

        // generate FABS
        summaryState.types.value?.forEach {
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

