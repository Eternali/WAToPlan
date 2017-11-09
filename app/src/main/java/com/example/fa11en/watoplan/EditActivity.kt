package com.example.fa11en.watoplan

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*


class EditActivity : Activity() {

    lateinit var bundle: Bundle
    lateinit var event: UserEvent
    lateinit var body: ParametersBody

    inner class TypeSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent == null || parent.getItemAtPosition(position) !in eventTypes.keys) return

            event = UserEvent(eventTypes[parent.getItemAtPosition(position)]!!)
            body.set(event.type.parameters)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    fun cancelEvent (view: View) = this.finish()

    fun saveEvent (view: View) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        bundle = intent.extras

        val typeName = bundle.getString("typeName")

        if (typeName == null || typeName !in eventTypes.keys) return
        event = UserEvent(eventTypes[typeName] as EventType)
        body = ParametersBody(this, event.type.parameters)

        val typeSpinner = findViewById<Spinner>(R.id.eventTypeSpinner)
        val typeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                                               eventTypes.keys.toList())
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter
        typeSpinner.setSelection(typeAdapter.getPosition(typeName))
        typeSpinner.onItemSelectedListener = TypeSelectedListener()

    }

}