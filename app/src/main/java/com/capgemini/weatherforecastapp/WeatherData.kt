package com.capgemini.weatherforecastapp

import org.json.JSONObject

data class City(val temp: Double,val temp_min: Double, val temp_max: Double)

data class Icon(val main:String,val description:String, val icon: String)
data class CurrentWeather(val main: City,val weather: List<Icon>)

data class DailyTemp(val day: Double,val min:Double, val max:Double, val night: Double, val eve: Double, val morn:Double)
data class Daily(val dt:Long,val temp: DailyTemp,val humidity: Int,val weather: List<Icon>)
data class DailyWeather(val lat: Double, val lon: Double, val daily: List<Daily>)

data class Coordinates(val name: String,val lat: Double,val lon: Double)

data class Hourly(val dt:Long, val temp: Double, val humidity: Int, val weather: List<Icon>)
data class HourlyTemp(val hourly: List<Hourly>)

//data class Coordinates(var lat:Double, val lon:Double)
