package com.example.fa11en.watoplan

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.view.ViewGroup
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

            }
            ParameterTypes.LOCATION -> {
                container = (parentContext as Activity).findViewById(R.id.eventLocationContainer)

            }
            ParameterTypes.ENTITIES -> {
                container = (parentContext as Activity).findViewById(R.id.eventEntitiesContainer)

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