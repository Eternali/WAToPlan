package com.example.fa11en.watoplan.views.dialog

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.example.fa11en.watoplan.*
import com.example.fa11en.watoplan.data.dataviewmodel.TypesViewModel
import com.example.fa11en.watoplan.viewmodels.EditTypeViewState
import com.example.fa11en.watoplan.views.EditTypeView
import kotlinx.android.synthetic.main.edittype_layout.*
import kotterknife.bindView
import yuku.ambilwarna.AmbilWarnaDialog


class EditTypeActivity: AppCompatActivity (), EditTypeView {

    // viewmodels

    lateinit override var state: EditTypeViewState
    lateinit override var types: TypesViewModel


    // use kotterknife to bind views to vals

    val name: EditText by bindView(R.id.nameEditText)
    val colorNormalButton: Button by bindView(R.id.typeColorNormalButton)
    val colorPressedButton: Button by bindView(R.id.typeColorPressedButton)
    val paramsContainer: LinearLayout by bindView(R.id.parametersContainer)
    val cancelButton: Button by bindView(R.id.typeCancelButton)
    val saveButton: Button by bindView(R.id.typeSaveButton)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edittype_layout)

        state = ViewModelProviders.of(this).get(EditTypeViewState::class.java)
        types = ViewModelProviders.of(this).get(TypesViewModel::class.java)

        render(this)
    }


    ////**** INTENTS ****////

    override fun fail(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    override fun saveType(ctx: Context) {
        if (state.typeName.value != null
                && state.typeColorNormal.value != null
                && state.typeColorPressed.value != null) {
            // assuming state.typeParams is initialized along with other state vals
            val eventType = EventType(state.typeName.value!!,
                    ArrayList(state.typeParams.keys.filter {
                        state.typeParams[it]?.value!!
                    }), state.typeColorNormal.value!!, state.typeColorPressed.value!!)

            if (state.isEdit.value == true) types.update(eventType)
            else types.add(eventType)
        } else fail(ctx, "Invalid parameters")
    }

    override fun deleteType(ctx: Context) {
        if (state.typeName.value == null) throw NullPointerException("Typename is empty.")
        if (types.value == null || types.value!!.value == null) throw NullPointerException("Types not loaded.")
        if (state.typeName.value!! in types.value!!.value!!.map { it.name })
            types.delete(types.getByName(state.typeName.value!!)!!)
        else
            showDbError(applicationContext, "Invalid Type Name")
    }

    override fun showColorChooser(ctx: Context, colorData: MutableLiveData<Int>) {
        class listener: AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                colorData.postValue(color)
            }

            override fun onCancel(dialog: AmbilWarnaDialog?) {
            }
        }

        AmbilWarnaDialog(ctx,
                if (colorData.value != null) colorData.value!!
                else ResourcesCompat.getColor(resources, R.color.colorAccent, null),
                listener()).dialog.show()
    }

    override fun render(ctx: Context) {

        //  initialize observables/listeners  //

        // wait until all typeParams have initialized to set checkboxes
        val paramsLoaded: Observer<Boolean> = Observer { _ ->
            state.typeParams.forEach {
                if (it.value.value == null) return@Observer
                else {
                    (paramsContainer.getChildAt(state.typeParams.keys.toList().indexOf(it.key)) as CheckBox)
                            .isChecked = it.value.value!!
                }
            }
        }
        state.typeParams.values.forEach { it.observe(this, paramsLoaded) }

        // name listener
        name.addTextChangedListener(TextParamWatcher(state.typeName))

        // color observer
        val colorNormalObserver: Observer<Int> = Observer {
            if (it != null)
                colorNormalButton.setBackgroundColor(it)
        }
        val colorPressedObserver: Observer<Int> = Observer {
            if (it != null)
                colorPressedButton.setBackgroundColor(it)
        }

        state.typeColorNormal.observe(this, colorNormalObserver)
        state.typeColorPressed.observe(this, colorPressedObserver)

        //  checkbox initialization and listeners  //
        for (c in 0 until paramsContainer.childCount) {
            paramsContainer.getChildAt(c).setOnClickListener {
                // assuming param checkbox text are only values of ParameterTypes.param
                state.typeParams[paramToParamType((it as CheckBox).text.toString())]
                        ?.postValue(!state.typeParams[paramToParamType(it.text.toString())]?.value!!)
            }
        }

        //  button listeners  //

        colorNormalButton.setOnClickListener {
            showColorChooser(ctx, state.typeColorNormal)
        }
        colorPressedButton.setOnClickListener {
            showColorChooser(ctx, state.typeColorPressed)
        }

        // isEdit observer
        val isEditObserver: Observer<Boolean> = Observer {
            if (it == true) {
                val type = types.getByName(intent.getStringExtra("typename"))

                nameEditText.setText(type!!.name)
                state.typeColorNormal.postValue(type.colorNormal)
                state.typeColorPressed.postValue(type.colorPressed)
                ParameterTypes.values().forEach {
                    if (it in type.parameters && state.typeParams[it]?.value != true)
                        state.typeParams[it]?.postValue(true)
                    else if (it !in type.parameters && state.typeParams[it]?.value != false)
                        state.typeParams[it]?.postValue(false)
                }

                // delete button
                cancelButton.text = getText(R.string.deleteText)
                cancelButton.setOnClickListener{
                    val code = Intent()
                    types.delete(types.getByName(type.name)!!)
                    setResult(ResultCodes.TYPEDELETED.code, code)
                    finish()
                }

                // update button
                saveButton.setOnClickListener {
                    val code = Intent()
                    saveType(ctx)
                    setResult(ResultCodes.TYPECHANGED.code, code)
                    finish()
                }
            } else {
                // cancel button
                cancelButton.setOnClickListener{
                    val code = Intent()
                    setResult(ResultCodes.TYPECANCELED.code, code)
                    finish()
                }

                // save button
                saveButton.setOnClickListener {
                    val code = Intent()
                    saveType(ctx)
                    setResult(ResultCodes.TYPESAVED.code, code)
                    finish()
                }
            }
        }
        types.value?.observe(this, Observer {
            if (it != null) state.isEdit.observe(this, isEditObserver)
        })

        // after everything is initialized then check if specific defaults set
        if (intent.hasExtra("typename"))
            state.isEdit.postValue(true)
        else state.isEdit.postValue(false)

    }

}