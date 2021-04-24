package com.capgemini.weatherforecastapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detailed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.Typography.degree

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var lat = 0.0
    var lng = 0.0
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            lat = it.getDouble("lat",0.0)
            lng = it.getDouble("lng",0.0)
            position = it.getInt("position")

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = "39df17a60fa57ff6678552d7458d80f6"
        val request = WeatherInterface.getInstance().getDaily(lat,lng,key)
        Toast.makeText(activity,"$lat, $lng",Toast.LENGTH_LONG).show()
        request.enqueue(DetailedCallback())
    }

    inner class DetailedCallback : Callback<DailyWeather>{
        override fun onResponse(call: Call<DailyWeather>, response: Response<DailyWeather>) {
            if(response.isSuccessful){
                val details = response.body()
                //Toast.makeText(activity,"${details}",Toast.LENGTH_LONG).show()

                val prefix = details?.daily?.get(position)

                val milliseconds = prefix?.dt
                val d = SimpleDateFormat("EEEE dd/MM/yyy HH:mm").format(milliseconds?.times(
                    1000
                )?.let { Date(it) })

                val imgUrl = "http://openweathermap.org/img/wn/${prefix?.weather?.get(0)?.icon}.png"

                activity?.let { Glide.with(it).load(Uri.parse(imgUrl)).into(iconV3) }

                detailsDateT.text = d.toString()
                deatailsTempT.text = (prefix?.temp?.day?.minus(273.15))?.toInt().toString() + "${degree}C"
                detailsMainT.text = prefix?.weather?.get(0)?.main
                detailsDescT.text = prefix?.weather?.get(0)?.description
                detailsMinMaxT.text = "${(prefix?.temp?.min?.minus(273.15))?.toInt().toString()}/" +
                        "${(prefix?.temp?.max?.minus(273.15))?.toInt().toString()}${degree}C"
                detailsHumidT.append(prefix?.humidity?.toString())
                mornT.text = (prefix?.temp?.morn?.minus(273.15))?.toInt().toString() + "${degree}C"
                eveT.text = (prefix?.temp?.eve?.minus(273.15))?.toInt().toString() + "${degree}C"
                nightT.text = (prefix?.temp?.night?.minus(273.15))?.toInt().toString() + "${degree}C"
            }
        }

        override fun onFailure(call: Call<DailyWeather>, t: Throwable) {
            Log.d("Details","some error: ${t.cause}, ${t.message}")
            Toast.makeText(activity,"some error: ${t.cause}, ${t.message}", Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DetailedFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}