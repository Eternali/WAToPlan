package com.example.fa11en.watoplan

import android.arch.lifecycle.MutableLiveData
import android.text.Editable
import android.text.TextWatcher


class EditParamWatcher (val toSave: MutableLiveData<Any>): TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

    override fun afterTextChanged(s: Editable?) {  }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when (toSave.value) {
            is String -> toSave.postValue(s.toString())
        }
    }

}