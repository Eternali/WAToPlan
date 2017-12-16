package com.chipthink.eternali.watoplan.data.dataviewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.chipthink.eternali.watoplan.AppDatabase
import com.chipthink.eternali.watoplan.EventsDB
import com.chipthink.eternali.watoplan.UserEvent


class UserEventsViewModel (app: Application) : AndroidViewModel (app) {

    var value: LiveData<List<UserEvent>>?

    var appdb: AppDatabase? = null
    init {
        appdb = EventsDB.getInstance(app)
        value = appdb?.eventDao()?.getAll()
    }

    fun getById (eid: Int): UserEvent? {
        return value?.value?.filter { it.eid == eid }!!.get(0)
    }

    fun add (vararg events: UserEvent) {
        events.forEach {
            if (appdb != null) AddAsyncTask(appdb!!).execute(it)
        }
    }

    fun delete (vararg events: Any) {
        events.forEach {
            if (appdb != null) DeleteAsyncTask(appdb!!).execute(it)
        }
    }

    companion object {

        private class AddAsyncTask (val db: AppDatabase) : AsyncTask<UserEvent, Unit, Unit>() {
            override fun doInBackground(vararg params: UserEvent?) {
                db.beginTransaction()
                params.forEach {
                    if (it == null) return@forEach
                    db.eventDao().insert(it)
                }
                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }
        private class DeleteAsyncTask (val db: AppDatabase) : AsyncTask<Any, Unit, Unit>() {

            override fun doInBackground(vararg params: Any?) {
                db.beginTransaction()
                params.forEach {
                    if (it == null) return@forEach
                    when (it) {
                        is UserEvent -> db.eventDao().delete(it)
                        is Int -> db.eventDao().deleteById(it)
                        is String -> db.eventDao().deleteByTypeName(it)
                    }
                }
                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }

    }

}

fun UserEventsViewModel.loadTypes (types: TypesViewModel) {
    this.value!!.value!!.forEach { it.type = types.getByName(it.typeName) }
}
