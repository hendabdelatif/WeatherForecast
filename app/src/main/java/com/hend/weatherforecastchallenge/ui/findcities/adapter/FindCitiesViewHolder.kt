package com.hend.weatherforecastchallenge.ui.findcities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.hend.weatherforecastchallenge.MainApplication.Companion.applicationContext
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.base.BaseViewHolder
import com.hend.weatherforecastchallenge.model.entities.CityWeather
import com.hend.weatherforecastchallenge.utils.IconProvider
import com.squareup.picasso.Picasso
import java.util.*

class FindCitiesViewHolder(itemView: View) : BaseViewHolder<CityWeather>(itemView) {

    companion object{
        fun create(parent: ViewGroup) = FindCitiesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_city_weather, parent, false)
        )
    }

    private var txtCityName: AppCompatTextView = itemView.findViewById(R.id.tv_city_name)
    private var txtWeatherDescription: AppCompatTextView =
        itemView.findViewById(R.id.tv_weather_description)
    private var txtWind: AppCompatTextView = itemView.findViewById(R.id.tv_wind)
    private var imgWeatherIcon: AppCompatImageView =
        itemView.findViewById(R.id.img_weather_icon)
    private var txtCurrentTemp: AppCompatTextView = itemView.findViewById(R.id.tv_current_temp)
    private var txtMaxTemp: AppCompatTextView = itemView.findViewById(R.id.tv_max_temp)
    private var txtMinTemp: AppCompatTextView = itemView.findViewById(R.id.tv_min_temp)


    override fun bind(item: CityWeather) {

        txtCityName.text = "%s, %s".format(Locale.getDefault(), item.name, item.sys.country)
        txtWeatherDescription.text = item.weather.get(0).description
        txtCurrentTemp.text = applicationContext.getString(R.string.temp_unit_label).format(
            Locale.getDefault(), item.main.temp)
        txtMaxTemp.text = applicationContext.getString(R.string.temp_unit_label).format(
            Locale.getDefault(), item.main.temp_max)
        txtMinTemp.text = applicationContext.getString(R.string.temp_unit_label).format(
            Locale.getDefault(), item.main.temp_min)
        txtWind.text = applicationContext.getString(R.string.wind_unit_label).format(
            Locale.getDefault(), item.wind.speed)


        val weatherDescription: String = item.weather.get(0).main
        Picasso.get().load(IconProvider.getImageIcon(weatherDescription)).into(imgWeatherIcon)
    }
}