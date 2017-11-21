package com.example.fa11en.watoplan.views.dialog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.fa11en.watoplan.R
import yuku.ambilwarna.AmbilWarnaDialog


class EditTypeActivity: AppCompatActivity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

}