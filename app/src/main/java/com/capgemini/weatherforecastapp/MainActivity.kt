package com.capgemini.weatherforecastapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.Typography.degree

class MainActivity : AppCompatActivity() {

    lateinit var lManager: LocationManager
    lateinit var locT: TextView
    var currentloc: Location? = null
    var latt: Double = 0.0
    var longi: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        locT = findViewById(R.id.cooredinateT)
        lManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val providerList = lManager.getProviders(true)
        var providerName = ""

        if(providerList.isNotEmpty()) {
            if(providerList.contains(LocationManager.GPS_PROVIDER)) {
                providerName = LocationManager.GPS_PROVIDER
            } else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
                providerName = LocationManager.NETWORK_PROVIDER
            } else {
                providerName = providerList[0]
            }
            //Toast.makeText(this,"Provider name: $providerName", Toast.LENGTH_LONG).show()

            val loc = lManager.getLastKnownLocation(providerName)
            if(loc != null){
                updateLocation(loc)
            } else {
                Toast.makeText(this,"No Location found", Toast.LENGTH_LONG).show()
            }
        }

        val key = "39df17a60fa57ff6678552d7458d80f6"
        val request = WeatherInterface.getInstance().getWeather(latt,longi,key)
        request.enqueue(TempCallback())

    }

    inner class TempCallback: Callback<CurrentWeather> {
        override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
            if(response.isSuccessful) {
                val details = response.body()
                //Toast.makeText(this@MainActivity,"$details",Toast.LENGTH_LONG).show()
                Log.d("Deatails","List $details")

                val min = details?.main?.temp_min?.minus(273.15)?.toInt().toString() + "${degree}C"
                val max = details?.main?.temp_max?.minus(273.15)?.toInt().toString() + "${degree}C"
                val imgUrl = "http://openweathermap.org/img/wn/${details?.weather?.get(0)?.icon}@2x.png"

                Glide.with(this@MainActivity).load(Uri.parse(imgUrl)).into(iconV)

                tempT.text = (details?.main?.temp?.minus(273.15))?.toInt().toString() + "${degree}C"
                minmaxT.text = "$min/$max"
                descT.text = details?.weather?.get(0)?.description
            }
        }

        override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
            Log.d("Details","some error: ${t.cause}, ${t.message}")
            Toast.makeText(this@MainActivity,"some error: ${t.cause}, ${t.message}",Toast.LENGTH_LONG).show()
        }

    }

    private fun updateLocation(loc: Location) {
        latt = loc.latitude
        longi = loc.longitude
        var distance:Float = 0f

        if(currentloc != null){

            distance = currentloc?.distanceTo(loc)!!
        }
        currentloc = loc
        locT.text = "$latt, $longi"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission(){

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),1)

        } else {
            //Toast.makeText(this,"Location permission granted", Toast.LENGTH_LONG).show()
        }
    }

    fun buttonClick(view: View) {
        val pref = getSharedPreferences("latloncity", MODE_PRIVATE)
        val editor = pref.edit()
        val i = Intent(this,NavigationActivity::class.java)
        when(view.id){
            R.id.jaiB -> {
                editor.putString("city","Jaipur")
                editor.putString("lat","26.8916")
                editor.putString("lng","75.7532")
                editor.commit()

                startActivity(i)
            }
            R.id.bomB -> {
                editor.putString("city","Mumbai")
                editor.putString("lat","19.0938779")
                editor.putString("lng","72.8070183")
                editor.commit()

                startActivity(i)
            }
            R.id.bangB -> {
                editor.putString("city","Bangalore")
                editor.putString("lat","12.9449891")
                editor.putString("lng","77.5447984")
                editor.commit()

                startActivity(i)
            }
            R.id.delB -> {
                editor.putString("city","Delhi")
                editor.putString("lat","28.6206232")
                editor.putString("lng","77.2141102")
                editor.commit()

                startActivity(i)
            }
            R.id.cityB -> {
                val city = cityE.text.toString().toLowerCase()
                val key = "39df17a60fa57ff6678552d7458d80f6"
                val request = WeatherInterface.getInstance().getCoordinate(city,1,key)
                request.enqueue(CoordinateCallback())
            }
        }
    }

    inner class CoordinateCallback: Callback<List<Coordinates>> {
        override fun onResponse(call: Call<List<Coordinates>>, response: Response<List<Coordinates>>) {
            if(response.isSuccessful) {
                val details = response.body()

                val i = Intent(this@MainActivity,NavigationActivity::class.java)

                val pref = getSharedPreferences("latloncity", MODE_PRIVATE)
                val editor = pref.edit()
                editor.putString("city",details?.get(0)?.name)
                editor.putString("lat", details?.get(0)?.lat!!.toString())
                editor.putString("lng", details?.get(0)?.lon!!.toString())
                editor.commit()

                startActivity(i)
            }
        }
        override fun onFailure(call: Call<List<Coordinates>>, t: Throwable) {
            Toast.makeText(this@MainActivity,"Error: ${t.cause}, ${t.message}",Toast.LENGTH_LONG).show()
        }
    }
}