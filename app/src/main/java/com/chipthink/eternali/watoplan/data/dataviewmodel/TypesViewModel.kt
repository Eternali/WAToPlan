package com.chipthink.eternali.watoplan.data.dataviewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.chipthink.eternali.watoplan.AppDatabase
import com.chipthink.eternali.watoplan.EventType
import com.chipthink.eternali.watoplan.EventsDB


class TypesViewModel (app: Application) : AndroidViewModel (app) {

    var value: LiveData<List<EventType>>?
    var appdb: AppDatabase? = null

    init {
        appdb = EventsDB.getInstance(app)
        value = appdb?.typeDao()?.getAll()
    }

    fun getByName (name: String): EventType? {
        return value?.value?.filter { it.name == name }?.get(0)
    }

    fun add (vararg types: EventType) {
        types.forEach {
            if (appdb != null) AddAsyncTask(appdb!!).execute(it)
        }
    }

    fun update (vararg types: EventType) {
        types.forEach {
            if (appdb != null) UpdateAsyncTask(appdb!!).execute(it)
        }
    }

    fun delete (vararg types: EventType) {
        types.forEach {
            if (appdb != null) DeleteAsyncTask(appdb!!).execute(it)
        }
    }

    companion object {
        private class AddAsyncTask (val db: AppDatabase) : AsyncTask<EventType, Unit, Unit>() {
            override fun doInBackground(vararg params: EventType?) {
                db.beginTransaction()
                params.forEach {
                    if (it == null) return@forEach
                    db.typeDao().insert(it)
                }
                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }

        private class UpdateAsyncTask (val db: AppDatabase) : AsyncTask<EventType, Unit, Unit>() {
            override fun doInBackground(vararg params: EventType?) {
                db.beginTransaction()
                params.forEach {
                    if (it == null) return@forEach
                    db.typeDao().update(it)
                }
                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }

        private class DeleteAsyncTask (val db: AppDatabase) : AsyncTask<EventType, Unit, Unit>() {
            override fun doInBackground(vararg params: EventType?) {
                db.beginTransaction()
                params.forEach {
                    if (it == null) return@forEach
                    db.typeDao().delete(it)
                }
                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }

    }

}