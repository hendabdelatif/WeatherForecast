package com.hend.weatherforecastchallenge.api

import com.hend.weatherforecastchallenge.model.entities.CityForecast
import com.hend.weatherforecastchallenge.model.entities.CityWeather
import com.hend.weatherforecastchallenge.utils.API_KEY
import com.hend.weatherforecastchallenge.utils.BASE_URL
import com.hend.weatherforecastchallenge.utils.UNITS
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton contains creating Retrofit instance and getters for APIs
 */
object NetworkApiServices {

    private val weatherApi: WeatherApi

    init {

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    fun getCityWeather(cityName: String): Observable<CityWeather> {
        return weatherApi.getCityWeather(cityName, UNITS, API_KEY)
    }

    fun getFiveDaysWeather(lat: Double, lon: Double): Single<CityForecast> {
        return weatherApi.getFiveDaysWeather(lat, lon, UNITS, API_KEY)
    }

}