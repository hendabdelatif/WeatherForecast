package com.hend.weatherforecastchallenge.ui.currentcity.adapter

import android.view.ViewGroup
import com.hend.weatherforecastchallenge.base.BaseRecyclerAdapter
import com.hend.weatherforecastchallenge.model.entities.Forecast

class CurrentCityAdapter(list: MutableList<Forecast>) :
    BaseRecyclerAdapter<Forecast, CurrentCityViewHolder>(list) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        CurrentCityViewHolder.create(
            parent
        )


}