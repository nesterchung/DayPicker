package com.nexterchung.daypicker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.DateFormatSymbols
import java.util.*

class DayAdapter : RecyclerView.Adapter<ViewHolder>() {

    val data = (1..7).toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(data[position])
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(day: Int) {
        val dayStr = DateFormatSymbols.getInstance(Locale.ENGLISH).shortWeekdays[day]
        (itemView as TextView).text = dayStr
    }
}
