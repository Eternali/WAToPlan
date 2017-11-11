package com.example.fa11en.watoplan

import android.app.FragmentTransaction
import android.arch.persistence.room.Room
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.ToggleButton
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import java.util.*


fun MutableList<UserEvent>.loadAll (db: AppDatabase) {
    this.clear()
    db.beginTransaction()
    try {
        this.addAll(db.eventDao().getAll())
        db.setTransactionSuccessful()
    } catch (e: Exception) {
        return
    } finally {
        db.endTransaction()
    }
}

fun MutableList<UserEvent>.saveAll (eventDao: UserEventDao) {
    this.forEach {
        eventDao.insert(it)
    }
}

fun MutableList<UserEvent>.deleteEvent (pos: Int, eventDao: UserEventDao) {
    eventDao.delete(this[pos])
    this.removeAt(pos)
}

fun MutableList<UserEvent>.addEvent (event: UserEvent, db: AppDatabase) : Boolean {
    db.beginTransaction()
    return try {
        db.eventDao().insert(event)
        this.add(event)
        db.setTransactionSuccessful()
        true
    } catch (e: Exception) {
        false
    } finally {
        db.endTransaction()
    }
}


enum class ParameterTypes (val param: String){
    TITLE ("TITLE"),
    DESCRIPTION ("DESCRIPTION"),
    DATETIME ("DATETIME"),
    LOCATION ("LOCATION"),
    ENTITIES ("ENTITIES"),
    REPEAT ("REPEAT")
}

internal val eventTypes = hashMapOf(
        "EVENT" to EventType("EVENT",
                                    arrayListOf(ParameterTypes.TITLE,
                                            ParameterTypes.DESCRIPTION,
                                            ParameterTypes.DATETIME,
                                            ParameterTypes.LOCATION,
                                            ParameterTypes.ENTITIES)),
        "EVALUATION" to EventType("EVALUATION",
                                    arrayListOf(ParameterTypes.TITLE,
                                            ParameterTypes.DESCRIPTION,
                                            ParameterTypes.DATETIME)),
        "PROJECT" to EventType("PROJECT",
                                    arrayListOf(ParameterTypes.TITLE,
                                            ParameterTypes.DESCRIPTION,
                                            ParameterTypes.DATETIME)),
        "REMINDER" to EventType("REMINDER",
                                    arrayListOf(ParameterTypes.TITLE,
                                            ParameterTypes.DESCRIPTION,
                                            ParameterTypes.DATETIME))
        )

internal var appdb: AppDatabase? = null
internal var events: MutableList<UserEvent> = ArrayList()

class MainActivity : AppCompatActivity() {

    fun getEvents (events: MutableList<UserEvent>, db: AppDatabase) {
        events.addEvent(UserEvent(events.size, eventTypes["EVENT"]!!), db)
        events[events.size-1].setParam(ParameterTypes.TITLE, "TEST TITLE")
        events[events.size-1].setParam(ParameterTypes.DESCRIPTION, "TEST DESCRIPTION")
        events[events.size-1].setParam(ParameterTypes.DATETIME, Calendar.getInstance())
        events[events.size-1].setParam(ParameterTypes.LOCATION, Location("gps"))
        events.loadAll(db)
    }

    val displayToggleListener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        for (r in 0..group.childCount) {
            if (group.getChildAt(r) == null) continue
            val view: ToggleButton = group.getChildAt(r) as ToggleButton
            view.isChecked = view.id == checkedId
        }
    }

    lateinit private var dotMenu: Menu
    lateinit var displayGroup: RadioGroup
    lateinit var dayToggle: ToggleButton
    lateinit var weekToggle: ToggleButton
    lateinit var monthToggle: ToggleButton

    lateinit var addMenu: FloatingActionsMenu
    val addMenuButtons: MutableList<FloatingActionButton> = mutableListOf()

    fun toggleDisplay (view: View) {
        displayGroup.clearCheck()
        displayGroup.check(view.id)

        val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (view.id) {
            R.id.dayToggle -> {
                fragTransaction.replace(R.id.displayFragContainer, DayFragment(), "Day")
            }
            R.id.weekToggle -> {
                fragTransaction.replace(R.id.displayFragContainer, WeekFragment(), "Week")
            }
            R.id.monthToggle -> {
                fragTransaction.replace(R.id.displayFragContainer, MonthFragment(), "Month")
            }
        }
        fragTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appdb = EventsDB.getInstance(this)
        getEvents(events, appdb!!)

        displayGroup = findViewById(R.id.overviewLayoutSwitcher)
        dayToggle = findViewById(R.id.dayToggle)
        weekToggle = findViewById(R.id.weekToggle)
        monthToggle = findViewById(R.id.monthToggle)

        displayGroup.setOnCheckedChangeListener(displayToggleListener)

        val fragTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragTransaction.replace(R.id.displayFragContainer, WeekFragment(), "Week")
        fragTransaction.commit()

        addMenu = findViewById<FloatingActionsMenu>(R.id.addMenu)

        // generate event adding buttons
        eventTypes.forEach {
            val button = FloatingActionButton(this)
            button.size = FloatingActionButton.SIZE_MINI
            button.setColorNormalResId(R.color.colorAccent)
            button.setColorPressedResId(R.color.colorAccent_pressed)
            button.title = it.key
            button.setOnClickListener { _ ->
                addMenu.collapse()
                val editIntent = Intent(this, EditActivity::class.java)
                editIntent.putExtra("typeName", it.key)
                startActivity(editIntent)
            }

            addMenuButtons.add(button)
            addMenu.addButton(addMenuButtons[addMenuButtons.size-1])
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu == null) return false
        menuInflater.inflate(R.menu.dot_menu, menu)
        dotMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return false
        when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }

        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            dotMenu.performIdentifierAction(R.id.dot_menu, 0)
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

}
