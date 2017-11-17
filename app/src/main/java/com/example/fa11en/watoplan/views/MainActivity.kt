package com.example.fa11en.watoplan

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
import com.example.fa11en.watoplan.views.SummaryView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import kotlinx.android.synthetic.main.activity_main.*
import kotterknife.bindView
import org.jetbrains.annotations.Nullable


/*
    Models:
        - mainViewState: eventTypes, userEvents, pos, displayType
        - editViewState: parameterTypes, userEvent
        -
 */

class MainActivity (mainState: SummaryViewState): AppCompatActivity (), SummaryView {

    // use kotterknife to bind views to vars

    // FAM
    private val addMenu: FloatingActionsMenu by bindView(R.id.addMenu)

    // view selection
    private val displayGroup: RadioGroup by bindView(R.id.displayToggleGroup)
    private val dayToggle: ToggleButton by bindView(R.id.dayToggle)
    private val weekToggle: ToggleButton by bindView(R.id.weekToggle)
    private val monthToggle: ToggleButton by bindView(R.id.monthToggle)

    lateinit private var dotMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        // set content view to layout
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        render(mainState,this)
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


    override fun settingsIntent(ctx: Context, event: UserEvent): UserEvent {

    }

    override fun addIntent(ctx: Context, type: EventType): EventType {

    }

    override fun render (summaryState: SummaryViewState, ctx: Context) {

        if (summaryState.nextActivity != MainActivity::class) {
            when (summaryState.nextActivity) {
                EditActivity::class -> startActivity(Intent(ctx, EditActivity::class.java))
                SettingsActivity::class -> startActivity(Intent(ctx, SettingsActivity::class.java))
            }
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
        mainState.eventTypes.forEach {
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

