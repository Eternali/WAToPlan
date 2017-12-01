package com.example.fa11en.watoplan

import android.app.FragmentTransaction
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.example.fa11en.watoplan.data.dataviewmodel.TypesViewModel
import com.example.fa11en.watoplan.data.dataviewmodel.UserEventsViewModel
import com.example.fa11en.watoplan.data.dataviewmodel.loadTypes
import com.example.fa11en.watoplan.viewmodels.SummaryViewState
import com.example.fa11en.watoplan.views.SummaryView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import kotlinx.android.synthetic.main.activity_main.view.*
import kotterknife.bindView
import java.util.*


/* TODO: Refactor strings.xml to use underscores instead of camelCase */


class MainActivity: AppCompatActivity (), SummaryView {

    // state variables
    lateinit override var state: SummaryViewState
    lateinit override var events: UserEventsViewModel
    lateinit override var types: TypesViewModel


    // use kotterknife to bind views to vals

    // FAM
    private val addMenu: FloatingActionsMenu by bindView(R.id.addMenu)
    // view selection
    private val displayGroup: RadioGroup by bindView(R.id.overviewLayoutSwitcher)
    // dot menu
    lateinit private var dotMenu: Menu


    override fun onCreate (savedInstanceState: Bundle?) {

        // set content view to layout
        // DEFAULT had parameter persistentState: PersistableBundle? in constructor and passed to super onCreate //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        state = ViewModelProviders.of(this).get(SummaryViewState::class.java)
        events = ViewModelProviders.of(this).get(UserEventsViewModel::class.java)
        types = ViewModelProviders.of(this).get(TypesViewModel::class.java)

        render(this)
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
                    reloadTypes()
                }
            }
            RequestCodes.NEWEVENT.code -> {
                if (resultCode == ResultCodes.EVENTSAVED.code) {
                    reloadEvents()
                }
            }
            RequestCodes.EDITEVENT.code -> {
                if (resultCode == ResultCodes.EVENTDELETED.code
                        || resultCode == ResultCodes.EVENTCHANGED.code) {
                    reloadEvents()
                }
            }
        }
    }


    ////**** INTENTS ****////

    fun generateFABS () {

        // handle event type additions or modifications
        types.value?.value?.forEach {
            val button = FloatingActionButton(this)
            button.size = FloatingActionButton.SIZE_MINI
            button.colorNormal = it.colorNormal
            button.colorPressed = it.colorPressed
            button.title = it.name
            button.setOnClickListener { _ ->
                addMenu.collapse()
                addIntent(this, it.name)
            }
            if (it.name in state.renderedFABs.keys) {
                addMenu.removeButton(state.renderedFABs[it.name])
                state.renderedFABs[it.name] = button
            }
            if (it.name !in state.renderedFABs) {
                state.renderedFABs.put(it.name, button)
            }

            addMenu.addButton(button)
        }

        // handle event type deletions
        val toremove = mutableListOf<String>()
        state.renderedFABs.forEach {
            if (types.value == null || types.value!!.value == null) return
            if (it.key !in types.value!!.value!!.map { it.name }) {
                addMenu.removeButton(it.value)
                toremove.add(it.key)
            }
        }
        toremove.forEach { state.renderedFABs.remove(it) }

    }

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    override fun reloadEvents() {
        events.loadTypes(types)
    }

    override fun reloadTypes() {

    }

    override fun toggleDisplay (viewid: Int) {
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

    override fun render (ctx: Context) {

        // observers
        val typesObserver: Observer<List<EventType>> = Observer {
            generateFABS()
        }
        val fragObserver: Observer<Int> = Observer {
            if (it != null && events.value != null) toggleDisplay(it)
        }
        val eventsObserver: Observer<List<UserEvent>> = Observer {
            if (it != null && types.value != null) {
                events.loadTypes(types)
                state.displayFrag.postValue(R.id.dateToggle)
            }
        }

        types.value?.observe(this, typesObserver)
        events.value?.observe(this, eventsObserver)
        state.displayFrag.observe(this, fragObserver)

        // make radio group from togglers

        displayGroup.dateToggle.setOnClickListener { state.displayFrag.postValue(it.id) }
        displayGroup.priorityToggle.setOnClickListener { state.displayFrag.postValue(it.id) }
        displayGroup.progressToggle.setOnClickListener { state.displayFrag.postValue(it.id) }

        displayGroup.setOnCheckedChangeListener({group, checkedId ->
            for (t in 0 until group.childCount) {
                if (group.getChildAt(t) == null) continue
                val view: ToggleButton = group.getChildAt(t) as ToggleButton
                view.isChecked = view.id == checkedId
            }
        })

    }

}

