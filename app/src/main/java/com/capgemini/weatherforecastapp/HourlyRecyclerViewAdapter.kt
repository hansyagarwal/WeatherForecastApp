package com.capgemini.weatherforecastapp

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.Typography.degree

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class HourlyRecyclerViewAdapter(
        private val values: List<Hourly>)
    : RecyclerView.Adapter<HourlyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_hourly_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val milliseconds = item.dt
        val d = SimpleDateFormat("EEEE dd/MM/yyy HH:mm").format(Date(milliseconds*1000))

        holder.timeT.text = d.toString()
        holder.tempT.text = (item.temp - 273.15).toInt().toString() + "${degree}C"
        holder.humidityT.text = "Humidity: ${item.humidity}"
        holder.descT.text = item.weather[0].description

        val imgUrl = "http://openweathermap.org/img/wn/${item.weather[0].icon}.png"

        Glide.with(holder.itemView.context).load(Uri.parse(imgUrl)).into(holder.icon)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeT = view.findViewById<TextView>(R.id.hourlyTimeT)
        val tempT = view.findViewById<TextView>(R.id.hourlyTempT)
        val descT = view.findViewById<TextView>(R.id.hourlyDescT)
        val humidityT = view.findViewById<TextView>(R.id.hourlyHumidityT)
        val icon = view.findViewById<ImageView>(R.id.iconV4)
    }
}