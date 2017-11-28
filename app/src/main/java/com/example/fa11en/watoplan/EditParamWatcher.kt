package com.example.fa11en.watoplan

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.fa11en.watoplan.views.EditView


class EditParamWatcher (var toSave: Any): TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

    override fun afterTextChanged(s: Editable?) {  }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when (toSave) {
            is String -> toSave = s.toString()
        }
    }

}