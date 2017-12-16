package com.chipthink.eternali.watoplan

import android.arch.lifecycle.MutableLiveData
import android.text.Editable
import android.text.TextWatcher


class TextParamWatcher (val toSave: MutableLiveData<String>): TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

    override fun afterTextChanged(s: Editable?) {  }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            toSave.postValue(s.toString())
    }

}