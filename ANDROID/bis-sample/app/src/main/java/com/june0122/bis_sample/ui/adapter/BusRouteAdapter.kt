package com.june0122.bis_sample.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.bis_sample.model.BusData
import com.june0122.bis_sample.ui.viewholder.BusRouteViewHolder

class BusRouteAdapter : RecyclerView.Adapter<BusRouteViewHolder>() {
    var items = arrayListOf<BusData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteViewHolder = BusRouteViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: BusRouteViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {


        }
    }
}