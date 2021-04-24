package com.capgemini.weatherforecastapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_daily_item_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class DailyForecastFragment : Fragment() {

    private var columnCount = 1
    var lat = 0.0
    var lng = 0.0
    lateinit var pref: SharedPreferences

    val dailyList = mutableListOf<Daily>()

    lateinit var dailyAdapter: DailyForecastRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        pref = activity?.getSharedPreferences("latloncity", Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                dailyAdapter = DailyForecastRecyclerViewAdapter(dailyList,
                    DailyWeather(lat,lng,dailyList)
                )
                adapter = dailyAdapter
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lat = pref.getString("lat","0")?.toDouble()!!
        lng = pref.getString("lng","0")?.toDouble()!!

        val key = "39df17a60fa57ff6678552d7458d80f6"
        val request = WeatherInterface.getInstance().getDaily(lat, lng,key)
        request.enqueue(DailyCallback())
    }

    inner class DailyCallback: Callback<DailyWeather> {
        override fun onResponse(call: Call<DailyWeather>, response: Response<DailyWeather>) {
            if(response.isSuccessful) {
                val dailydetails = response.body()

                dailydetails?.daily?.let {
                    list.adapter = DailyForecastRecyclerViewAdapter(it,DailyWeather(lat,lng,dailyList))
                }
            }
        }

        override fun onFailure(call: Call<DailyWeather>, t: Throwable) {
            Log.d("Details","some error: ${t.cause}, ${t.message}")
            Toast.makeText(activity,"some error: ${t.cause}, ${t.message}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            DailyForecastFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}