package com.example.fa11en.watoplan

import android.app.FragmentTransaction
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
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


class MainActivity: AppCompatActivity (), SummaryView {

    // use kotterknife to bind views to vals

    // FAM
    private val addMenu: FloatingActionsMenu by bindView(R.id.addMenu)
    override val typesRendered: MutableLiveData<MutableList<EventType>> = MutableLiveData()

    // view selection
    private val displayGroup: RadioGroup by bindView(R.id.overviewLayoutSwitcher)

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
        typesRendered.postValue(mutableListOf())
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
                settingsIntent(applicationContext)
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
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodes.ISEVENTTYPECHANGED.code -> {
                if (resultCode == ResultCodes.TYPESAVED.code
                        || resultCode == ResultCodes.TYPEDELETED.code
                        || resultCode == ResultCodes.TYPECHANGED.code) {
                    SummaryViewState.Loading.destroyInstance()
                    render(SummaryViewState.Loading.getInstance
                    (true, false, false), this)
                }
            }
            RequestCodes.NEWEVENT.code -> {
                if (resultCode == ResultCodes.EVENTSAVED.code) {
                    SummaryViewState.Loading.destroyInstance()
                    render(SummaryViewState.Loading.getInstance
                    (true, true, false), this)
                }
            }
            RequestCodes.EDITEVENT.code -> {
                if (resultCode == ResultCodes.EVENTDELETED.code
                        || resultCode == ResultCodes.EVENTCHANGED.code) {
                    SummaryViewState.Loading.destroyInstance()
                    render(SummaryViewState.Loading.getInstance
                    (true, true, false), this)
                }
            }
        }
    }


    ////**** INTENTS ****////

    fun generateFABS (state: SummaryViewState, isRemove: Boolean = false) {
        state.types.value?.forEach {
            val button = FloatingActionButton(this)
            button.size = FloatingActionButton.SIZE_MINI
            button.colorNormal = it.colorNormal
            button.colorPressed = it.colorPressed
            button.title = it.name
            button.setOnClickListener { _ ->
                addMenu.collapse()
                addIntent(this, it.name)
            }
            if (isRemove) {
                addMenu.removeButton(button)
                typesRendered.value!!.remove(it)
            } else {
                addMenu.addButton(button)
                typesRendered.value!!.add(it)
            }
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

    override fun toggleDisplay (viewid: Int, state: SummaryViewState) {
        displayGroup.clearCheck()
        displayGroup.check(viewid)

        // TODO: only replace fragment when previous is different from requested
        val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (viewid) {
            R.id.dateToggle -> {
//                val ordered = SummaryViewState.events.value?.orderByDate()
//                if (ordered != null && null !in ordered)
//                    SummaryViewState.events.postValue(ordered.map { it!! })
                fragTransaction.replace(R.id.displayFragContainer, OrderDateFragment(), "date")
                fragTransaction.commit()
            }
            R.id.priorityToggle -> {
//                val ordered = SummaryViewState.events.value?.orderByPriority()
//                if (ordered != null && null !in ordered)
//                    SummaryViewState.events.postValue(ordered.map { it!! })
                fragTransaction.replace(R.id.displayFragContainer, OrderPriorityFragment(), "priority")
                fragTransaction.commit()
            }
            R.id.progressToggle -> {
//                val ordered = SummaryViewState.events.value?.orderByProgress()
//                if (ordered != null && null !in ordered)
//                    SummaryViewState.events.postValue(ordered.map { it!! })
                fragTransaction.replace(R.id.displayFragContainer, OrderProgressFragment(), "process")
                fragTransaction.commit()
            }
            else -> fragTransaction.commit()
        }
    }

    override fun addIntent (ctx: Context, typeName: String) {
        val adder = Intent(ctx, EditActivity::class.java)
        adder.putExtra("typename", typeName)
        startActivityForResult(adder, RequestCodes.NEWEVENT.code)
    }

    override fun settingsIntent (ctx: Context) {
        startActivityForResult(Intent(ctx, SettingsActivity::class.java), RequestCodes.ISEVENTTYPECHANGED.code)
    }

    override fun render (state: SummaryViewState, ctx: Context) {

        // fragment handling
        when (state) {
            is SummaryViewState.Loading -> {

                val dataLoadedObserver: Observer<Boolean> = Observer {
                    if (state.typesLoaded.value != true)
                        loadTypes(state)
                    if (state.typesLoaded.value == true && state.eventsLoaded.value != true)
                        loadEvents(state)
                    if (state.typesLoaded.value == true && state.eventsLoaded.value == true) {
                        render(SummaryViewState.Passive(displayGroup.dateToggle.id), ctx)
                    }
                }
                // watch loading status
                val dbLoadingObserver: Observer<Boolean> = Observer {
                    // == true for null safety
                    if (it == true) {
                        state.typesLoaded.observe(this, dataLoadedObserver)
                        state.eventsLoaded.observe(this, dataLoadedObserver)
                    }
                }
                val typesLoadingObserver: Observer<List<EventType>> = Observer {
                    if (it == null)
                        showDbError(ctx, "Failed to load Event Types")
                    else {
                        state.typesLoaded.postValue(true)
                        generateFABS(state, true)
                        generateFABS(state)
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

                // bind observables
                state.dbLoaded.observe(this, dbLoadingObserver)
                state.types.observe(this, typesLoadingObserver)
                SummaryViewState.events.observe(this, eventsLoadingObserver)

                // load database if not already done so
                if (state.dbLoaded.value != true) {
                    if (loadDatabase(ctx)) state.dbLoaded.postValue(true)
                    else showDbError(ctx, "Failed to load Database")
                }

            }
            is SummaryViewState.Passive -> {
                // set display fragment
                val fragObserver: Observer<Int> = Observer {
                    if (it != null) toggleDisplay(it, state)
                }
                val eventsObserver: Observer<List<UserEvent>> = Observer {

                }

                state.displayFrag.observe(this, fragObserver)
                SummaryViewState.events.observe(this, eventsObserver)
            }
        }

        // make radio group from togglers

        displayGroup.dateToggle.setOnClickListener { toggleDisplay(it.id, state) }
        displayGroup.priorityToggle.setOnClickListener { toggleDisplay(it.id, state) }
        displayGroup.progressToggle.setOnClickListener { toggleDisplay(it.id, state) }

        displayGroup.setOnCheckedChangeListener({group, checkedId ->
            for (t in 0..group.childCount) {
                if (group.getChildAt(t) == null) continue
                val view: ToggleButton = group.getChildAt(t) as ToggleButton
                view.isChecked = view.id == checkedId
            }
        })

    }

}

