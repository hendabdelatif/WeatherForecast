package com.hend.weatherforecastchallenge.model.repository

import com.hend.weatherforecastchallenge.api.NetworkApiServices
import com.hend.weatherforecastchallenge.model.entities.CityForecast
import com.hend.weatherforecastchallenge.model.entities.CityWeather
import io.reactivex.Observable
import io.reactivex.Single

class Repository() {

    /**
     * Get Observable of City Weather when search cities by name
     */
    fun getCityWeather(cityName: String): Observable<CityWeather> {
        return NetworkApiServices.getCityWeather(cityName)
    }

    /**
     * Get Observable of Five Days Forecast for the current city
     */
    fun getFiveDaysWeather(lat: Double, lon: Double): Single<CityForecast> {
        return NetworkApiServices.getFiveDaysWeather(lat, lon)
    }
}