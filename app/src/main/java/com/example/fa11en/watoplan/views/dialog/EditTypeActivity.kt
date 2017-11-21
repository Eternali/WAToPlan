package com.example.fa11en.watoplan.views.dialog

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.fa11en.watoplan.*
import com.example.fa11en.watoplan.viewmodels.EditTypeViewState
import com.example.fa11en.watoplan.views.EditTypeView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edittype_layout)

        render(EditTypeViewState(), this)
    }

    class listener: AmbilWarnaDialog.OnAmbilWarnaListener {
        override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCancel(dialog: AmbilWarnaDialog?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    val colorDialog = AmbilWarnaDialog(this, R.color.colorAccent, listener()).dialog.show()

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

            appdb.beginTransaction()
            try {
                appdb.typeDao().insert(eventType)
                true
            } catch (e: Exception) {
                false
            } finally {
                appdb.endTransaction()
            }
        } else false
    }

    override fun showDbError(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    override fun render(state: EditTypeViewState, ctx: Context) {
        // initialize observables


        //  radio listeners  //
        for (c in 0 until paramsContainer.childCount) {
            paramsContainer.getChildAt(c).setOnClickListener {
                state.typeParams[ParameterTypes.valueOf(
                        it.toString())]?.postValue(!state.typeParams[ParameterTypes.valueOf(it.toString())]?.value!!)
            }
        }

        //  button listeners  //

        colorNormalButton.setOnClickListener {  }
        colorPressedButton.setOnClickListener {  }

        cancelButton.setOnClickListener{
            val code = Intent()
            setResult(ResultCodes.TYPECANCELED.code, code)
            finish()
        }
        saveButton.setOnClickListener {
            val code = Intent()
            if (saveType(state))
                setResult(ResultCodes.TYPESAVED.code, code)
            else
                setResult(ResultCodes.TYPEFAILED.code, code)
            finish()
        }

    }

}