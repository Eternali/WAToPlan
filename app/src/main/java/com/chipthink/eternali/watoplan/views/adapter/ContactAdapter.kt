package com.chipthink.eternali.watoplan

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class ContactAdapter (val ctx: Context, var resource: Int, var people: MutableList<Person>)
        : ArrayAdapter<Person> (ctx, resource, people) {

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            val personView = PersonListView(ctx)

            val name = personView.findViewById<TextView>(R.id.nameInList)
            name.text = "${people[position].firstName} ${people[position].lastName}"

            return personView
        }

        return convertView
    }

}