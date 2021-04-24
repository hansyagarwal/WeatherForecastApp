package com.capgemini.weatherforecastapp

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_city.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var lat = 0.0
    var lng = 0.0
    var city = ""
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        pref = activity?.getSharedPreferences("latloncity",MODE_PRIVATE)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        city = pref.getString("city","").toString()
        lat = pref.getString("lat","0")?.toDouble()!!
        lng = pref.getString("lng","0")?.toDouble()!!

        citycT.text = city

        val key = "39df17a60fa57ff6678552d7458d80f6"
        val request = WeatherInterface.getInstance().getWeather(lat,lng,key)
        request.enqueue(TempCallback())
    }

    inner class TempCallback: Callback<CurrentWeather> {
        override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
            if(response.isSuccessful) {
                val details = response.body()
                Log.d("Deatails","List $details")

                val min = details?.main?.temp_min?.minus(273.15)?.toInt().toString() + "${Typography.degree}C"
                val max = details?.main?.temp_max?.minus(273.15)?.toInt().toString() + "${Typography.degree}C"
                val imgUrl = "http://openweathermap.org/img/wn/${details?.weather?.get(0)?.icon}@2x.png"

                Glide.with(this@CityFragment).load(Uri.parse(imgUrl)).into(iconv5)

                tempcT.text = (details?.main?.temp?.minus(273.15))?.toInt().toString() + "${Typography.degree}C"
                minmaxcT.text = "$min/$max"
                maincT.text = details?.weather?.get(0)?.main
                desccT.text = details?.weather?.get(0)?.description
            }
        }

        override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
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
         * @return A new instance of fragment CityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CityFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}