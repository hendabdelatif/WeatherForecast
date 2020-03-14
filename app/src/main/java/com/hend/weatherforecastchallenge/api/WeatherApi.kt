package com.hend.weatherforecastchallenge.api

import com.hend.weatherforecastchallenge.model.entities.CityForecast
import com.hend.weatherforecastchallenge.model.entities.CityWeather
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    fun getCityWeather(@Query("q") cityName: String, @Query("units") units: String, @Query("appid") apiKey: String): Observable<CityWeather>

    @GET("forecast")
    fun getFiveDaysWeather(@Query("lat") lat: Double, @Query("lon") lon: Double,@Query("units") units: String, @Query("appid") apiKey: String): Single<CityForecast>

}
