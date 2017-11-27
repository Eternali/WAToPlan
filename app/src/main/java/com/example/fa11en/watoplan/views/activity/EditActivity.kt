package com.example.fa11en.watoplan

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.example.fa11en.watoplan.viewmodels.EditViewState
import com.example.fa11en.watoplan.views.EditTypeView
import com.example.fa11en.watoplan.views.EditView
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import kotlinx.android.synthetic.main.activity_edit.view.*
import kotterknife.bindView
import java.util.*


class EditActivity : AppCompatActivity (), EditView {

    // get state variables
    lateinit override var appdb: AppDatabase
    override val state = EditViewState()

    // initialize ui bindings
    private val saveButton: Button by bindView(R.id.saveButton)
    private val cancelButton: Button by bindView(R.id.cancelButton)
    private val typeSpinner: Spinner by bindView(R.id.eventTypeSpinner)

    // initialize parameter views and mappings
    private val titleContainer: LinearLayout by bindView(R.id.eventTitleContainer)
    private val descriptionContainer: LinearLayout by bindView(R.id.eventDescContainer)
    private val datetimeContainer: LinearLayout by bindView(R.id.eventDateTimeContainer)
    private val locationContainer: LinearLayout by bindView(R.id.eventLocationContainer)
    private val entitiesContainer: LinearLayout by bindView(R.id.eventEntitiesContainer)
    private val repeatContainer: LinearLayout by bindView(R.id.eventRepeatContainer)

    override val paramtoView: LinkedHashMap<ParameterTypes, LinearLayout> = linkedMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        render(applicationContext)
    }

    inner class TypeSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent == null || parent.getItemAtPosition(position) !in state.types.map { it.name })
                return

            state.curType.postValue(state.types[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }


    ////**** INTENTS ****////

    override fun loadDatabase(ctx: Context): Boolean {
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

    override fun fail(ctx: Context, msg: String) {
        showDbError(ctx, msg)
        val code = Intent()
        setResult(ResultCodes.TYPEFAILED.code, code)
        finish()
    }

    override fun loadTypes(): Boolean {
        appdb.beginTransaction()
        return try {
            state.types = appdb.typeDao().getAll()
            appdb.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            false
        } finally {
            appdb.endTransaction()
        }
    }

    override fun setType(typeName: String): Boolean {
        // initialize spinner
        val typeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                state.types.map { it.name })
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter

        appdb.beginTransaction()
        return try {
            val type = appdb.typeDao().get(typeName)
            state.curType.postValue(type)
            appdb.setTransactionSuccessful()

            typeSpinner.setSelection(typeAdapter.getPosition(type.name))
            true
        } catch (e: Exception) {
            typeSpinner.setSelection(0)
            false
        } finally {
            appdb.endTransaction()
            typeSpinner.onItemSelectedListener = TypeSelectedListener()
        }
    }

    override fun getEvent(eid: Int): UserEvent? {
        appdb.beginTransaction()
        return try {
            val event = appdb.eventDao().get(eid)
            appdb.setTransactionSuccessful()
            event
        } catch (e: Exception) {
            null
        } finally {
            appdb.endTransaction()
        }
    }

    override fun saveEvent(ctx: Context): Boolean {
        appdb.beginTransaction()
        return try {
            // initialize event with the state
            if (state.curType.value == null) fail(ctx, "Event Type Not Set")
            val event = UserEvent(state.curType.value!!)

            // set event parameters
            state.params.value!!.keys.forEach {
                event.setParam(it, state.params.value!![it]?.value!!)
            }

            // save event to database
            if (state.isEdit.value == true) appdb.eventDao().update(event)
            else appdb.eventDao().insert(event)
            appdb.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            false
        } finally {
            appdb.endTransaction()
        }
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
                    val date = Calendar.getInstance()
                    date.set(Calendar.YEAR, year)
                    date.set(Calendar.MONTH, month)
                    date.set(Calendar.DAY_OF_MONTH, day)
                    state.params.value!![ParameterTypes.DATETIME]!!.postValue(date)
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
        paramtoView.put(ParameterTypes.DATETIME, datetimeContainer)
        paramtoView.put(ParameterTypes.LOCATION, locationContainer)
        paramtoView.put(ParameterTypes.ENTITIES, entitiesContainer)
        paramtoView.put(ParameterTypes.REPEAT, repeatContainer)

        // add parameter container specific button event listeners
        // must defer displaying popup until after all lifecycle methods are called
        datetimeContainer.timeButton.setOnClickListener { timeChooser(it) }
        datetimeContainer.dateButton.setOnClickListener { dateChooser(it) }
        locationContainer.eventLocationEdit.setOnClickListener { mapDialog(it) }

        // initialize observers
        val loadingObserver: Observer<Boolean> = Observer {
            if (it == true) {
                if (intent.hasExtra("eid"))
                    state.isEdit.postValue(true)
                else if (intent.hasExtra("typename"))
                    state.isEdit.postValue(false)
                else fail(ctx, "No Type or Event Specified")
            }
        }
        val isEditObserver: Observer<Boolean> = Observer {
            if (it == true) {
                val event: UserEvent? = getEvent(intent.extras.getInt("eid"))
                if (event == null) fail(ctx, "Invalid Event ID")
                else if (!setType(event.typeName)) fail(ctx, "Invalid Type Name")
            } else {
                if (!setType(intent.extras.getString("typename")))
                    fail(ctx, "Invalid Type Name")
            }
        }
        val typeObserver: Observer<EventType> = Observer { type ->
            Log.i("TYPE", type?.parameters.toString())
            ParameterTypes.values().forEach {
                if (type?.parameters!!.contains(it)) {
                    paramtoView[it]?.visibility = LinearLayout.VISIBLE
                    state.params.value?.put(it, EditViewState.initializeParam(it, true))
                } else {
                    paramtoView[it]?.visibility = LinearLayout.GONE
                    state.params.value?.remove(it)
                }
            }
        }

        state.loaded.observe(this, loadingObserver)
        state.isEdit.observe(this, isEditObserver)
        state.curType.observe(this, typeObserver)

        // parameter string data observers
        titleContainer.eventTitleEdit.addTextChangedListener()
        descriptionContainer.eventDescEdit.addTextChangedListener()

        // initialize data
        if (loadDatabase(ctx))
            state.loaded.postValue(loadTypes())
        else {
            fail(ctx, "Failed to Load the Database")
        }

        // ender clickers

        if (state.isEdit.value == true)
            cancelButton.text = getString(R.string.deleteText)
        else
            cancelButton.text = getString(R.string.cancelText)

        cancelButton.setOnClickListener {
            val code = Intent()
            if (state.isEdit.value == true)
                setResult(ResultCodes.TYPEDELETED.code, code)
            else
                setResult(ResultCodes.TYPECANCELED.code, code)
            finish()
        }
        saveButton.setOnClickListener {
            val code = Intent()
            if (saveEvent(ctx)) setResult(ResultCodes.TYPESAVED.code, code)
            else setResult(ResultCodes.TYPEFAILED.code, code)
            finish()
        }

    }

}