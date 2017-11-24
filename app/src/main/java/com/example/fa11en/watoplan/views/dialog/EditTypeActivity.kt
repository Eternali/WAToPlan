package com.example.fa11en.watoplan.views.dialog

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.example.fa11en.watoplan.*
import com.example.fa11en.watoplan.viewmodels.EditTypeViewState
import com.example.fa11en.watoplan.views.EditTypeView
import kotlinx.android.synthetic.main.edittype_layout.*
import kotterknife.bindView
import yuku.ambilwarna.AmbilWarnaDialog


class EditTypeActivity: AppCompatActivity (), EditTypeView {

    // use kotterknife to bind views to vals

    val name: EditText by bindView(R.id.nameEditText)
    val colorNormalButton: Button by bindView(R.id.typeColorNormalButton)
    val colorPressedButton: Button by bindView(R.id.typeColorPressedButton)
    val paramsContainer: LinearLayout by bindView(R.id.parametersContainer)
    val cancelButton: Button by bindView(R.id.typeCancelButton)
    val saveButton: Button by bindView(R.id.typeSaveButton)

    override lateinit var appdb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edittype_layout)

        if (EditTypeViewState.Edit.isInstantiated())
            render(EditTypeViewState.Edit.getInstance(null), this)
        else
            render(EditTypeViewState.New(), this)
    }


    ////**** INTENTS ****////

    override fun saveType(state: EditTypeViewState): Boolean {
        return if (state.typeName.value != null
                && state.typeColorNormal.value != null
                && state.typeColorPressed.value != null) {
            // assuming state.typeParams is initialized along with other state vals
            val eventType = EventType(state.typeName.value!!,
                    ArrayList(state.typeParams.keys.filter {
                        state.typeParams[it]?.value!!
                    }), state.typeColorNormal.value!!, state.typeColorPressed.value!!)
//            appdb.beginTransaction()
            try {
                appdb.typeDao().insert(eventType)
                true
            } catch (e: Exception) {
                false
            } finally {
//                appdb.endTransaction()
            }
        } else false
    }

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
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

    override fun render(state: EditTypeViewState, ctx: Context) {

        // get database TODO fix db
        appdb = EventsDB.getInstance(ctx)

        //  initialize observables/listeners  //

        // name listener
        name.addTextChangedListener(EditParamWatcher(state.typeName))

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

        //  checkbox listeners  //
        for (c in 0 until paramsContainer.childCount) {
            paramsContainer.getChildAt(c).setOnClickListener {
                // assuming param radio button text are only values of ParameterTypes.param
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

        cancelButton.setOnClickListener{
            val code = Intent()
            setResult(ResultCodes.TYPECANCELED.code, code)
            finish()
        }
        saveButton.setOnClickListener {
            saveType(state)
            val code = Intent()
            if (saveType(state))
                setResult(ResultCodes.TYPESAVED.code, code)
            else
                setResult(ResultCodes.TYPEFAILED.code, code)
            finish()
        }

        // after everything is initialized then check if specific defaults set
        when (state) {
            is EditTypeViewState.Edit -> {
                nameEditText.setText(state.typeName.value)
                colorNormalButton.setBackgroundColor(state.typeColorNormal.value!!)
                colorPressedButton.setBackgroundColor(state.typeColorPressed.value!!)
            }
        }

    }

}