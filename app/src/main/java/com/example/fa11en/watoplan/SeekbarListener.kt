package com.example.fa11en.watoplan

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.widget.SeekBar


class SeekbarListener (val toSave: MutableLiveData<Int>): SeekBar.OnSeekBarChangeListener {

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        Log.i("asdf", toSave.value.toString())
        toSave.postValue(progress)
    }

}