package com.capgemini.weatherforecastapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_daily.*

//THIS CLASS IS NOT IN USE

class DailyActivity : AppCompatActivity() {

    var lat = 0.0
    var lng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        val b = intent.extras
        val city = b?.getString("city")
        lat = b?.getDouble("lat")!!
        lng = b?.getDouble("lng")
        val bundle = Bundle()

        if(city!=null){
            tv2.append("$city")
        }

        if (lat != null) {
            bundle.putDouble("lat",lat)
        }
        if (lng != null) {
            bundle.putDouble("lng",lng)
            Toast.makeText(this,"$city, $lat, $lng",Toast.LENGTH_LONG).show()
        }


        val frag = DailyForecastFragment()
        frag.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.dailyL,frag)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("Hourly Forecast")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "Hourly Forecast" -> {
                val bundle = Bundle()
                bundle.putDouble("lat",lat)
                bundle.putDouble("lng",lng)

                dailyL.visibility = View.INVISIBLE

                val frag = HourlyFragment()
                frag.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.dailyLayout,frag).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}