package com.hend.weatherforecastchallenge.model.entities

/**
 * Response of getCityWeather() API when search cities by name
 */
data class CityWeather(val coord: Coordinates, val weather: List<Weather>, val main : Temperature, val wind: Wind, val sys : Sys, val timezone : Int, val id : Int, val name : String)

data class Coordinates(val lat : Double,val lon : Double)

data class Weather(val id: Int, val main : String, val description : String, val icon : String)

data class Temperature(val temp : Double, val feels_like : Double, val temp_min : Double, val temp_max : Double, val pressure : Int, val humidity : Int)

data class Wind(val speed : Double, val deg : Int)

data class Sys(val id : Int, val country : String)


