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
import kotlinx.android.synthetic.main.fragment_hourly_item_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class HourlyFragment : Fragment() {

    private var columnCount = 1
    var lat = 0.0
    var lng = 0.0

    lateinit var pref: SharedPreferences

    val hourlyList = mutableListOf<Hourly>()
    lateinit  var hourlyAdapter : HourlyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        pref = activity?.getSharedPreferences("latloncity", Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hourly_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                hourlyAdapter = HourlyRecyclerViewAdapter(hourlyList)
                adapter = hourlyAdapter
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lat = pref.getString("lat","0")?.toDouble()!!
        lng = pref.getString("lng","0")?.toDouble()!!

        val key = "39df17a60fa57ff6678552d7458d80f6"
        val request = WeatherInterface.getInstance().getHourly(lat,lng,key)
        request.enqueue(hourlyCallback())
    }

    inner class hourlyCallback : Callback<HourlyTemp> {
        override fun onResponse(call: Call<HourlyTemp>, response: Response<HourlyTemp>) {
            if(response.isSuccessful){
                val details = response.body()

                details?.hourly?.let {
                    list.adapter = HourlyRecyclerViewAdapter(it)
                }
            }
        }

        override fun onFailure(call: Call<HourlyTemp>, t: Throwable) {
            Log.d("Hourly","some error: ${t.cause}, ${t.message}")
            Toast.makeText(activity,"some error: ${t.cause}, ${t.message}", Toast.LENGTH_LONG).show()
        }

    }


    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                HourlyFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}