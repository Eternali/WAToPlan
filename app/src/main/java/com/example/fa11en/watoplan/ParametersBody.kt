package com.example.fa11en.watoplan

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView


class ParametersBody (val parentContext: Context,
                      private var params: MutableList<ParameterTypes>) {

    init {
        params.forEach {
            renderParam(it)
        }
    }

    fun set (parameters: MutableList<ParameterTypes>) {
        ParameterTypes.values().forEach {
            if (it !in params && it in parameters) renderParam(it)
            else if (it in params && it !in parameters) removeParam(it)
        }
    }

    fun renderParam (param: ParameterTypes) {
        val container: LinearLayout
        when (param) {
            ParameterTypes.TITLE -> {
                container = (parentContext as Activity).findViewById(R.id.eventTitleContainer)
                val labelText = TextView(parentContext)
                val titleEdit = EditText(parentContext)

                labelText.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.4f)
                labelText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                labelText.text = param.param
                titleEdit.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.6f)
                titleEdit.hint = param.param.toLowerCase()

                container.addView(labelText)
                container.addView(titleEdit)
            }
            ParameterTypes.DESCRIPTION -> {
                container = (parentContext as Activity).findViewById(R.id.eventDescContainer)
                val labelText = TextView(parentContext)
                val descEdit = EditText(parentContext)

                labelText.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.4f)
                labelText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                labelText.text = param.param
                descEdit.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.6f)
                descEdit.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                descEdit.hint = param.param.toLowerCase()

                container.addView(labelText)
                container.addView(descEdit)
            }
            ParameterTypes.DATETIME -> {
                container = (parentContext as Activity).findViewById(R.id.eventDateTimeContainer)
                val labelContainer = LinearLayout(parentContext)
                val buttonContainer = LinearLayout(parentContext)
                val labelText = TextView(parentContext)
                val timeButton = Button(parentContext)
                val dateButton = Button(parentContext)

                labelContainer.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
                buttonContainer.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val labelTextParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
                labelTextParams.setMargins(0, 0, 0, 12)
                labelText.layoutParams = labelTextParams
                labelText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                labelText.text = parentContext.getString(R.string.datetimeLabelText)
                timeButton.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.5f
                )
                timeButton.text = parentContext.getString(R.string.editTimeButton)
                dateButton.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.5f
                )
                dateButton.text = parentContext.getString(R.string.editDateButton)

                labelContainer.addView(labelText)
                buttonContainer.addView(timeButton)
                buttonContainer.addView(dateButton)
                container.addView(labelContainer)
                container.addView(buttonContainer)
            }
            ParameterTypes.LOCATION -> {
                container = (parentContext as Activity).findViewById(R.id.eventLocationContainer)

            }
            ParameterTypes.ENTITIES -> {
                container = (parentContext as Activity).findViewById(R.id.eventEntitiesContainer)
                val labelText = TextView(parentContext)

                labelText.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0.4f)
                labelText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                labelText.text = parentContext.getString(R.string.entityLabelText)

                container.addView(labelText)
            }
            ParameterTypes.REPEAT -> {
                container = (parentContext as Activity).findViewById(R.id.eventRepeatContainer)

            }
        }
    }

    fun removeParam (param: ParameterTypes) {
        val container: LinearLayout
        when (param) {
            ParameterTypes.TITLE      -> container = (parentContext as Activity).findViewById(R.id.eventTitleContainer)
            ParameterTypes.DESCRIPTION-> container = (parentContext as Activity).findViewById(R.id.eventDescContainer)
            ParameterTypes.DATETIME   -> container = (parentContext as Activity).findViewById(R.id.eventDateTimeContainer)
            ParameterTypes.LOCATION   -> container = (parentContext as Activity).findViewById(R.id.eventLocationContainer)
            ParameterTypes.ENTITIES   -> container = (parentContext as Activity).findViewById(R.id.eventEntitiesContainer)
            ParameterTypes.REPEAT     -> container = (parentContext as Activity).findViewById(R.id.eventRepeatContainer)
        }
        container.removeAllViewsInLayout()
    }

}