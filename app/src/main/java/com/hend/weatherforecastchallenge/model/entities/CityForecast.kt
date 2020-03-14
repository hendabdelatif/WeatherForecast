package com.hend.weatherforecastchallenge.model.entities

/**
 * Response of getFiveDaysWeather() API when fetching current location and get 5 days 3 hours for the current city
 */
data class CityForecast(val list : List<Forecast>, val city: City)

data class Forecast(val main : Temperature, val weather: List<Weather>, val wind: Wind, val dt_txt : String)

data class City(val id: Int, val name: String, val coord : Coordinates, val country : String, val timezone: Int)
