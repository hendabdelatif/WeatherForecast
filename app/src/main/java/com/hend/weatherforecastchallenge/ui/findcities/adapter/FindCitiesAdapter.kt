package com.hend.weatherforecastchallenge.ui.findcities.adapter

import android.view.ViewGroup
import com.hend.weatherforecastchallenge.base.BaseRecyclerAdapter
import com.hend.weatherforecastchallenge.model.entities.CityWeather

class FindCitiesAdapter(list: MutableList<CityWeather>) :
    BaseRecyclerAdapter<CityWeather, FindCitiesViewHolder>(list) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        FindCitiesViewHolder.create(
            parent
        )


}