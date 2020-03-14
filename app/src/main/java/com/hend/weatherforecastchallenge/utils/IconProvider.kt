package com.hend.weatherforecastchallenge.utils

import com.hend.weatherforecastchallenge.R

object IconProvider {
    fun getImageIcon(weatherDescription: String?): Int {
        return when (weatherDescription) {
            "Thunderstorm" -> R.mipmap.ic_atmosphere
            "Drizzle" -> R.mipmap.ic_drizzle
            "Rain" -> R.mipmap.ic_rain
            "Snow" -> R.mipmap.ic_snow
            "Atmosphere" -> R.mipmap.ic_atmosphere
            "Clear" -> R.mipmap.ic_clear
            "Clouds" -> R.mipmap.ic_cloudy
            "Extreme" -> R.mipmap.ic_extreme
            else -> R.mipmap.ic_launcher
        }
    }
}