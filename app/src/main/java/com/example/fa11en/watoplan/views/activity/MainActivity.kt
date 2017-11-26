package com.example.fa11en.watoplan

import android.app.FragmentTransaction
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
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


/* TODO: Refactor strings.xml to use underscores instead of camelCase */


// global enum of themes
enum class Themes {
    DARK,
    LIGHT
}

// global enum for request codes
enum class RequestCodes (val code: Int) {
    NEWEVENTTYPE(0),
    EDITEVENTTYPE(1),
    EVENTTYPECHANGED(2),
    EDITEVENT(3),
    NEWEVENT(4)
}

// global enum for result codes
enum class ResultCodes (val code: Int) {
    TYPECANCELED(0),
    TYPESAVED(1),
    TYPEFAILED(2),
    TYPEDELETED(3),
    TYPECHANGED(4),
    EVENTADDED(5),
    EVENTCHANGED(6)
}

// This can be put here because it will never change and is required globally
enum class ParameterTypes (val param: String) {
    TITLE ("TITLE"),
    DESCRIPTION ("DESCRIPTION"),
    DATETIME ("DATETIME"),
    LOCATION ("LOCATION"),
    ENTITIES ("PEOPLE"),
    REPEAT ("REPETITIONS")
}

// This must be defined to get ParameterTypes object based on its param
fun paramToParamType (param: String): ParameterTypes {
    ParameterTypes.values()
            .filter { it.param == param }
            .forEach { return it }
    throw TypeNotPresentException(param, Throwable())
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

    override fun onCreate (savedInstanceState: Bundle?) {

        // set content view to layout
        // DEFAULT had parameter persistentState: PersistableBundle? in constructor and passed to super onCreate //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* I HAVE TWO OPTIONS TO MANAGE STATE:
        *  1: Use ViewModelProviders to keep a lifecycle aware instance of the model alive across fragments
        *       (but this requires vararg parameter in render method to specify how to set the state)
        *  2: Make each SummaryViewState subclass a singleton and get the instance across fragments
        *       (but this is not lifecycle aware)
        *       -- there is an issue with this where the instance persists across soft restarts of the app (i think)
        *  I have gone with the 2nd option for now */
        render(SummaryViewState.Loading.getInstance
                (false, false, false), this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("RESULT", "SAVE RESULTED TO RELOAD DISPLAY")
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodes.EVENTTYPECHANGED.code -> {
                if (resultCode == ResultCodes.TYPECHANGED.code
                        || resultCode == ResultCodes.TYPESAVED.code
                        || resultCode == ResultCodes.TYPEDELETED.code) {
                    render(SummaryViewState.Loading.getInstance
                    (false, false, false), this)
                }
            }
        }
    }


    ////**** INTENTS ****////

    fun generateFABS (types: List<EventType>) {
        types.forEach {
            val button = FloatingActionButton(this)
            button.size = FloatingActionButton.SIZE_MINI
            button.colorNormal = it.colorNormal
            button.colorPressed = it.colorPressed
            button.title = it.name
            button.setOnClickListener { _ ->
                addMenu.collapse()
                addIntent(this, it.name)
            }
            addMenu.addButton(button)
        }
    }

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

    override fun loadTypes (state: SummaryViewState): Boolean {
        appdb.beginTransaction()
        return try {
//            appdb.typeDao().insert(EventType("TEST", mutableListOf(ParameterTypes.TITLE, ParameterTypes.DESCRIPTION),
//                    ResourcesCompat.getColor(resources, R.color.colorAccent, null),
//                    ResourcesCompat.getColor(resources, R.color.colorAccent_pressed, null)))
            state.types.postValue(appdb.typeDao().getAll())
            Log.i("TYPES", state.types.value.toString())
            appdb.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            showDbError(applicationContext, "Failed to load Event Types")
            false
        } finally {
            appdb.endTransaction()
        }
    }

    override fun loadEvents (state: SummaryViewState): Boolean {
        appdb.beginTransaction()
        return try {
//            val event = UserEvent("TestType")
//            event.loadType(db)
//            event.setParam(ParameterTypes.TITLE, "TEST TITLE")
//            event.setParam(ParameterTypes.DESCRIPTION, "TEST DESCRIPTION")
//            db.eventDao().insert(event)
//            val gets: List<UserEvent> = db.eventDao().getAll()
//            if (gets[0] != event) throw TypeNotPresentException(event.typeName, Throwable())
            state.events.postValue(appdb.eventDao().getAll())
            appdb.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            showDbError(applicationContext, "Failed to load Events")
            false
        } finally {
            appdb.endTransaction()
        }
    }

    override fun toggleDisplay (view: View) {
        displayGroup.clearCheck()
        displayGroup.check(view.id)

        render(SummaryViewState.Passive(view.id), this)
    }

    override fun editIntent (ctx: Context, eid: Int) {
        val editor = Intent(ctx, EditActivity::class.java)
        editor.putExtra("eid", eid)
        startActivityForResult(Intent(ctx, EditActivity::class.java), RequestCodes.EDITEVENT.code)
    }

    override fun addIntent (ctx: Context, typeName: String) {
        val adder = Intent(ctx, EditActivity::class.java)
        adder.putExtra("typename", typeName)
        startActivityForResult(Intent(ctx, EditActivity::class.java), RequestCodes.NEWEVENT.code)
    }

    override fun settingsIntent (ctx: Context) {
        startActivityForResult(Intent(ctx, SettingsActivity::class.java), RequestCodes.EVENTTYPECHANGED.code)
    }

    override fun render (state: SummaryViewState, ctx: Context) {

        // fragment handling
        when (state) {
            is SummaryViewState.Loading -> {

                // watch loading status
                val dbLoadingObserver: Observer<Boolean> = Observer {
                    if (it == true) {
                        // must have != true to accommodate null case
                        if (state.typesLoaded.value != true) loadTypes(state)
                    }
                }
                val typesLoadingObserver: Observer<List<EventType>> = Observer {
                    if (it == null)
                        showDbError(ctx, "Failed to load Event Types")
                    else {
                        state.typesLoaded.postValue(true)
                        generateFABS(it)
                        if (state.eventsLoaded.value != true) loadEvents(state)
                    }
                }
                val eventsLoadingObserver: Observer<List<UserEvent>> = Observer {
                    if (it == null)
                        showDbError(ctx, "Failed to load Events")
                    else if (state.typesLoaded.value == true) {
                        it.forEach {
                            if (!it.loadType(appdb))
                                throw TypeNotPresentException(it.typeName, Throwable())
                        }
                        state.eventsLoaded.postValue(true)
                    }
                }

                val finishedLoadingObserver: Observer<Boolean> = Observer {
                    if (state.typesLoaded.value == true && state.eventsLoaded.value == true) {
                        render(SummaryViewState.Passive(dayToggle.id), ctx)
                    }
                }

                // bind observables
                state.dbLoaded.observe(this, dbLoadingObserver)
                state.types.observe(this, typesLoadingObserver)
                state.events.observe(this, eventsLoadingObserver)
                state.typesLoaded.observe(this, finishedLoadingObserver)
                state.eventsLoaded.observe(this, finishedLoadingObserver)

                // load database if not already done so
                if (state.dbLoaded.value != true) {
                    if (loadDatabase(ctx)) state.dbLoaded.postValue(true)
                    else showDbError(ctx, "Failed to load Database")
                }

            }
            is SummaryViewState.Passive -> {
                // set display fragment
                // TODO: only replace fragment when previous is different from requested
                val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                when (state.displayFrag.value) {
                    R.id.dayToggle -> {
                        fragTransaction.replace(R.id.displayFragContainer, DayFragment(), "day")
                        fragTransaction.commit()
                        render(SummaryViewState.DayViewModel.getInstance(0), this)
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
            is SummaryViewState.DayViewModel -> {  }
            is SummaryViewState.WeekViewModel -> {  }
            is SummaryViewState.MonthViewModel -> {  }
        }

        // make radio group from togglers
        displayGroup.setOnCheckedChangeListener({group, checkedId ->
            for (t in 0..group.childCount) {
                if (group.getChildAt(t) == null) continue
                val view: ToggleButton = group.getChildAt(t) as ToggleButton
                view.isChecked = view.id == checkedId
            }
        })

    }

}

