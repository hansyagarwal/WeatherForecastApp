package com.capgemini.weatherforecastapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherInterface {

    @GET("/data/2.5/weather")
    fun getWeather(@Query("lat") lat:Double, @Query("lon") lon:Double, @Query("appid") key:String) : Call<CurrentWeather>
    //fun getWeather(@Query("appid") key:String) : Call<Nqwe>

    @GET("/data/2.5/onecall")
    fun getDaily(@Query("lat") lat:Double, @Query("lon") lon:Double, @Query("appid") key:String) : Call<DailyWeather>

    @GET("geo/1.0/direct")
    fun getCoordinate(@Query("q") q: String,@Query("limit") limit:Int, @Query("appid") key:String) : Call<List<Coordinates>>

    @GET("/data/2.5/onecall")
    fun getHourly(@Query("lat") lat:Double, @Query("lon") lon:Double, @Query("appid") key:String) : Call<HourlyTemp>

    companion object{
        val BASE_URL = "http://api.openweathermap.org"

        fun getInstance() : WeatherInterface{

            val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)

            val retrofit = builder.build()
            return retrofit.create(WeatherInterface::class.java)
        }
    }
}