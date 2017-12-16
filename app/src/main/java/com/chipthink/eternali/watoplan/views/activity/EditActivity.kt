package com.chipthink.eternali.watoplan

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.chipthink.eternali.watoplan.data.dataviewmodel.TypesViewModel
import com.chipthink.eternali.watoplan.data.dataviewmodel.UserEventsViewModel
import com.chipthink.eternali.watoplan.viewmodels.EditViewState
import com.chipthink.eternali.watoplan.views.EditView
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.view.*
import kotterknife.bindView
import java.util.*
import kotlin.collections.LinkedHashMap


class EditActivity : AppCompatActivity (), EditView {

    // get state variables
    lateinit override var state: EditViewState
    lateinit override var types: TypesViewModel
    lateinit override var events: UserEventsViewModel

    override val paramtoView: LinkedHashMap<ParameterTypes, LinearLayout> = linkedMapOf()


    // initialize ui bindings
    private val saveButton: Button by bindView(R.id.saveButton)
    private val cancelButton: Button by bindView(R.id.cancelButton)

    private val typeSpinner: Spinner by bindView(R.id.eventTypeSpinner)
    // initialize parameter views and mappings
    private val titleContainer: LinearLayout by bindView(R.id.eventTitleContainer)
    private val descriptionContainer: LinearLayout by bindView(R.id.eventDescContainer)
    private val priorityContainer: LinearLayout by bindView(R.id.eventPriorityContainer)
    private val progressContainer: LinearLayout by bindView(R.id.eventProgressContainer)
    private val datetimeContainer: LinearLayout by bindView(R.id.eventDateTimeContainer)
    private val notiContainer: LinearLayout by bindView(R.id.eventNotiContainer)
    private val locationContainer: LinearLayout by bindView(R.id.eventLocationContainer)
    private val entitiesContainer: LinearLayout by bindView(R.id.eventEntitiesContainer)

    private val repeatContainer: LinearLayout by bindView(R.id.eventRepeatContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        state = ViewModelProviders.of(this).get(EditViewState::class.java)
        events = ViewModelProviders.of(this).get(UserEventsViewModel::class.java)
        types = ViewModelProviders.of(this).get(TypesViewModel::class.java)

        render(this)
    }

    inner class TypeSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (types.value == null || types.value!!.value == null
                || parent == null || parent.getItemAtPosition(position) !in types.value!!.value!!.map { it.name })
                return

            state.curType.postValue(types.value!!.value!![position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }


    ////**** INTENTS ****////

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    override fun fail(ctx: Context, msg: String) {
        showDbError(ctx, msg)
        val code = Intent()
        setResult(ResultCodes.TYPEFAILED.code, code)
        finish()
    }

    override fun setType(ctx: Context, typeName: String) {
        Log.i("aslkj", typeName)
        // initialize spinner
        val typeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                types.value?.value?.map { it.name })
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter

        val type = types.getByName(typeName)
        if (type == null) fail(ctx, "Type does not exist.")

        state.curType.postValue(type)
        typeSpinner.setSelection(typeAdapter.getPosition(type?.name))
        typeSpinner.onItemSelectedListener = TypeSelectedListener()
    }

    override fun saveEvent(ctx: Context, eid: Int?) {
        if (state.curType.value == null) fail(ctx, "Event Type Not Set")
        val event = UserEvent(state.curType.value!!)
        // delete existing event if we're editing it (update isn't working)
        if (eid != null) {
            event.eid = eid
            events.delete(event.eid)
        }

        // set event parameters
        event.setParam(ParameterTypes.TITLE, state.name.value as Any)
        event.setParam(ParameterTypes.DESCRIPTION, state.desc.value as Any)
        event.setParam(ParameterTypes.DATETIME, state.datetime.value as Any)
        event.setParam(ParameterTypes.NOTIS, state.notis.value as Any)
        event.setParam(ParameterTypes.LOCATION, state.location.value as Any)
        event.setParam(ParameterTypes.ENTITIES, state.entities.value as Any)
        event.setParam(ParameterTypes.REPEAT, state.repetitions.value as Any)
        event.setParam(ParameterTypes.PROGRESS, state.progress.value as Any)
        event.setParam(ParameterTypes.PRIORITY, state.priority.value as Any)

        // save event to database
        events.add(event)
    }

    override fun timeChooser(view: View) {
        val curTime = Calendar.getInstance()
        val cHour = curTime.get(Calendar.HOUR_OF_DAY)
        val cMinute = curTime.get(Calendar.MINUTE)

        val timeDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { view, hour, minute -> run {
                    val labelText = findViewById<TextView>(R.id.eventDatetimeLabel)
                    val text = labelText.text.split(", ") as MutableList<String>
                    text[0] = hour.toString() + ": " + minute.toString()
                    labelText.text = text.joinToString(", ")
                    state.datetime.value?.set(Calendar.HOUR_OF_DAY, hour)
                    state.datetime.value?.set(Calendar.MINUTE, minute)
                } }, cHour, cMinute, true)
        timeDialog.setTitle("Select Time")
        timeDialog.show()
    }

    override fun dateChooser(view: View) {
        val curDate = Calendar.getInstance()
        val cYear = curDate.get(Calendar.YEAR)
        val cMonth = curDate.get(Calendar.MONTH)
        val cDay = curDate.get(Calendar.DAY_OF_MONTH)
        val dateDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, day -> run {
                    val labelText = findViewById<TextView>(R.id.eventDatetimeLabel)
                    val text = labelText.text.split(", ") as MutableList<String>
                    text[1] = day.toString() + " " + month.toString() + " " + year.toString()
                    labelText.text = text.joinToString(", ")
                    state.datetime.value?.set(Calendar.YEAR, year)
                    state.datetime.value?.set(Calendar.MONTH, month)
                    state.datetime.value?.set(Calendar.DAY_OF_MONTH, day)
                } }, cYear, cMonth, cDay)
        dateDialog.setTitle("Select Date")
        dateDialog.show()
    }

    override fun mapDialog(view: View) {
        // create dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_map)
        dialog.show()

        // set up view
        val mapView = dialog.findViewById<MapView>(R.id.mapViewOnEdit)
        MapsInitializer.initialize(this)
        mapView.onCreate(dialog.onSaveInstanceState())
        mapView.onResume()
        val googleMap = mapView.getMapAsync( {

        } )
    }

    override fun render(ctx: Context) {

        // initialize containers
        paramtoView.put(ParameterTypes.TITLE, titleContainer)
        paramtoView.put(ParameterTypes.DESCRIPTION, descriptionContainer)
        paramtoView.put(ParameterTypes.PRIORITY, priorityContainer)
        paramtoView.put(ParameterTypes.PROGRESS, progressContainer)
        paramtoView.put(ParameterTypes.DATETIME, datetimeContainer)
        paramtoView.put(ParameterTypes.NOTIS, notiContainer)
        paramtoView.put(ParameterTypes.LOCATION, locationContainer)
        paramtoView.put(ParameterTypes.ENTITIES, entitiesContainer)
        paramtoView.put(ParameterTypes.REPEAT, repeatContainer)

        // add parameter container specific button event listeners
        // must defer displaying popup until after all lifecycle methods are called
        datetimeContainer.timeButton.setOnClickListener { timeChooser(it) }
        datetimeContainer.dateButton.setOnClickListener { dateChooser(it) }
        locationContainer.eventLocationEdit.setOnClickListener { mapDialog(it) }

        // initialize observers
        val typeObserver: Observer<EventType> = Observer { type ->
            Log.i("name", type?.name)
            ParameterTypes.values().forEach {
                if (type?.parameters!!.contains(it)) {
                    paramtoView[it]?.visibility = LinearLayout.VISIBLE
                } else {
                    paramtoView[it]?.visibility = LinearLayout.GONE
                }
            }
        }
        val isEditObserver: Observer<Boolean> = Observer {
            if (it == true && events.value?.value != null) {
                // set cancel button text and functionality
                cancelButton.text = getString(R.string.deleteText)

                val event = events.getById(intent.extras.getInt("eid"))
                Log.i("EVENT", event.toString())
                if (event == null) fail(ctx, "Invalid Event ID")
                else setType(ctx, event.typeName)

                // set defaults according to event
                event!!.params.keys.forEach {
                    when (it) {
                        ParameterTypes.TITLE -> titleContainer.eventTitleEdit.setText(event.params[it] as String)
                        ParameterTypes.DESCRIPTION -> descriptionContainer.eventDescEdit.setText(event.params[it] as String)
                        ParameterTypes.PRIORITY -> priorityContainer.eventPrioritySeek.progress = event.params[it] as Int
                        ParameterTypes.PROGRESS -> progressContainer.eventProgressSeek.progress = event.params[it] as Int
                        ParameterTypes.DATETIME -> {
                            datetimeContainer.eventDatetimeLabel.text = getString(R.string.dateFormattedText,
                                    (event.params[it] as Calendar).timestr(), (event.params[it] as Calendar).datestr())
                            state.datetime.postValue(event.params[it] as Calendar)
                        }
                        ParameterTypes.NOTIS -> state.notis.postValue((event.params[it] as List<Int>).toMutableList())
                        ParameterTypes.LOCATION -> state.location.postValue(event.params[it] as Location)
                        ParameterTypes.ENTITIES -> state.entities.postValue((event.params[it] as List<Person>).toMutableList())
                        ParameterTypes.REPEAT -> state.repetitions.postValue((event.params[it] as List<Calendar>).toMutableList())
                    }
                }
            } else {
                cancelButton.text = getString(R.string.cancelText)
                setType(ctx, intent.extras.getString("typename"))
            }
        }

        types.value?.observe(this, Observer {
            if (it != null)
                state.curType.observe(this, typeObserver)
        })
        events.value?.observe(this, Observer {
            if (it != null)
                state.isEdit.observe(this, isEditObserver)
        })

        // param observers
        val locationObserver: Observer<Location> = Observer {

        }
        val entitiesObserver: Observer<List<Person>> = Observer {

        }
        val repetitionsObserver: Observer<List<Calendar>> = Observer {

        }

        state.location.observe(this, locationObserver)
//        state.entities.observe(this, entitiesObserver)
//        state.repetitions.observe(this, repetitionsObserver)

        // text onchange listeners
        titleContainer.eventTitleEdit.addTextChangedListener(TextParamWatcher(state.name))
        descriptionContainer.eventDescEdit.addTextChangedListener(TextParamWatcher(state.desc))

        // seekbar listeners
        eventPriorityContainer.eventPrioritySeek.setOnSeekBarChangeListener(SeekbarListener(state.priority))
        eventProgressContainer.eventProgressSeek.setOnSeekBarChangeListener(SeekbarListener(state.progress))

        // ender clickers
        cancelButton.setOnClickListener {
            val code = Intent()
            if (state.isEdit.value == true) {
                events.delete(intent.extras.getInt("eid"))
                setResult(ResultCodes.EVENTDELETED.code, code)
            }
            else
                setResult(ResultCodes.EVENTCANCELED.code, code)
            finish()
        }
        saveButton.setOnClickListener {
            val code = Intent()
            if (state.isEdit.value == true) {
                saveEvent(ctx, intent.extras.getInt("eid"))
                setResult(ResultCodes.EVENTCHANGED.code, code)
            } else {
                saveEvent(ctx, null)
                setResult(ResultCodes.EVENTSAVED.code, code)
            }
            finish()
        }

        if (intent.hasExtra("eid"))
            state.isEdit.postValue(true)
        else state.isEdit.postValue(false)

    }

}