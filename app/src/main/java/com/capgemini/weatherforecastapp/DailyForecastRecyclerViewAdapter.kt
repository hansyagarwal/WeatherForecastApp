package com.capgemini.weatherforecastapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_daily.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.text.Typography.degree

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class DailyForecastRecyclerViewAdapter(
    private val values: List<Daily>,val q : DailyWeather
) : RecyclerView.Adapter<DailyForecastRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_daily_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val lat = q.lat
        val lng = q.lon

        val milliseconds = item.dt
        val d = SimpleDateFormat("EEEE dd/MM/yyy").format(Date(milliseconds*1000))
        val imgUrl = "http://openweathermap.org/img/wn/${item.weather[0].icon}.png"

        Glide.with(holder.itemView.context).load(Uri.parse(imgUrl)).into(holder.iconV2)

        holder.dateT.text = d.toString()
        holder.dailyminmaxT.text = "${(item.temp.min - 273.15).toInt().toString()}/${(item.temp.max - 273.15).toInt().toString()} ${degree}C"

        val card = holder.itemView.findViewById<CardView>(R.id.detailsCard)
        card.setOnClickListener {
            val bundle = Bundle()

            val activity : AppCompatActivity = it.context as AppCompatActivity
            val frag = DetailedFragment()

            bundle.putDouble("lat",lat)
            bundle.putDouble("lng",lng)
            bundle.putInt("position",position)

            frag.arguments = bundle
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment,frag)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateT = view.findViewById<TextView>(R.id.dateT)
        val iconV2 = view.findViewById<ImageView>(R.id.iconV2)
        val dailyminmaxT = view.findViewById<TextView>(R.id.dailyminmaxT)

        /*override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/
    }
}