package com.hend.weatherforecastchallenge.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Type, ViewHolder : BaseViewHolder<Type>>(list: List<Type> = mutableListOf()) : RecyclerView.Adapter<ViewHolder>() {

    var items: MutableList<Type> = list.toMutableList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position]) }