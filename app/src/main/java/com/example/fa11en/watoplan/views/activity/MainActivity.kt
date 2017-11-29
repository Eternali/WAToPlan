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
import java.util.*


/* TODO: Refactor strings.xml to use underscores instead of camelCase */


// global enum of themes
enum class Themes {
    DARK,
    LIGHT
}

// global enum for request codes
enum class RequestCodes (val code: Int) {
    NEWEVENTTYPE(100),
    EDITEVENTTYPE(101),
    EVENTTYPECHANGED(102),
    EDITEVENT(103),
    NEWEVENT(104)
}

// global enum for result codes
enum class ResultCodes (val code: Int) {
    TYPECANCELED(200),
    TYPESAVED(201),
    TYPEFAILED(202),
    TYPEDELETED(203),
    TYPECHANGED(204),
    EVENTADDED(205),
    EVENTCHANGED(206)
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


// Location extension methods to generate printable text
fun Calendar.timestr (): String {
    return this.get(Calendar.HOUR_OF_DAY).toString() + ": " + this.get(Calendar.MINUTE)
}

fun Calendar.datestr (): String {
    return this.get(Calendar.DAY_OF_MONTH).toString() + " " + this.get(Calendar.MONTH) + " " + this.get(Calendar.YEAR)
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
//        super.onActivityResult(requestCode, resultCode, data)
        Log.i("REQUESTCODE", "asdfadfasdfasdfa")
        when (requestCode) {
            RequestCodes.EVENTTYPECHANGED.code -> {
                if (resultCode == ResultCodes.TYPECHANGED.code
                        || resultCode == ResultCodes.TYPESAVED.code
                        || resultCode == ResultCodes.TYPEDELETED.code) {
                    Log.i("RESULT", "SAVE RESULTED TO RELOAD DISPLAY")
                    SummaryViewState.Loading.destroyInstance()
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
            SummaryViewState.events.postValue(appdb.eventDao().getAll())
            Log.i("EVENTS", appdb.eventDao().getAll()[0].params.toString())
            appdb.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            showDbError(applicationContext, "Failed to load Events")
            false
        } finally {
            appdb.endTransaction()
        }
    }

    override fun toggleDisplay (viewid: Int) {
        displayGroup.clearCheck()
        displayGroup.check(viewid)

        // TODO: only replace fragment when previous is different from requested
        val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (viewid) {
            R.id.dayToggle -> {
                fragTransaction.replace(R.id.displayFragContainer, DayFragment(), "day")
                fragTransaction.commit()
            }
            R.id.weekToggle -> {
                fragTransaction.replace(R.id.displayFragContainer, WeekFragment(), "day")
                fragTransaction.commit()
            }
            R.id.monthToggle -> {
                fragTransaction.replace(R.id.displayFragContainer, MonthFragment(), "day")
                fragTransaction.commit()
            }
            else -> fragTransaction.commit()
        }
    }

    override fun editIntent (ctx: Context, eid: Int) {
        val editor = Intent(ctx, EditActivity::class.java)
        editor.putExtra("eid", eid)
        startActivityForResult(editor, RequestCodes.EDITEVENT.code)
    }

    override fun addIntent (ctx: Context, typeName: String) {
        val adder = Intent(ctx, EditActivity::class.java)
        adder.putExtra("typename", typeName)
        startActivityForResult(adder, RequestCodes.NEWEVENT.code)
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
                SummaryViewState.events.observe(this, eventsLoadingObserver)
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
                val fragObserver: Observer<Int> = Observer {
                    if (it != null) toggleDisplay(it)
                }
                state.displayFrag.observe(this, fragObserver)
            }
        }

        // make radio group from togglers

        displayGroup.dayToggle.setOnClickListener { toggleDisplay(it.id) }
        displayGroup.weekToggle.setOnClickListener { toggleDisplay(it.id) }
        displayGroup.monthToggle.setOnClickListener { toggleDisplay(it.id) }

        displayGroup.setOnCheckedChangeListener({group, checkedId ->
            for (t in 0..group.childCount) {
                if (group.getChildAt(t) == null) continue
                val view: ToggleButton = group.getChildAt(t) as ToggleButton
                view.isChecked = view.id == checkedId
            }
        })

    }

}

