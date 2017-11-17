package com.example.fa11en.watoplan

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
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
    private val addMenu: FloatingActionsMenu by bindView(R.id.addMenu)


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        // set content view to layout
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        render(mainState,this)

    }

    override fun editIntent(ctx: Context, event: UserEvent): UserEvent {

    }

    override fun addIntent(ctx: Context, type: EventType): EventType {

    }

    override fun render (summaryState: SummaryViewState, ctx: Context) {
        // get togglers from layout
        val displayGroup = findViewById<RadioGroup>(R.id.displayToggleGroup)
        val dayToggle = findViewById<ToggleButton>(R.id.dayToggle)
        val weekToggle = findViewById<ToggleButton>(R.id.weekToggle)
        val monthToggle = findViewById<ToggleButton>(R.id.monthToggle)

        // make radio group from togglers
        val displayToggleListener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener
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

