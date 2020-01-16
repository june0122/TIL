package com.june0122.bis_sample

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StationBusListAdapter : RecyclerView.Adapter<StationBusListViewHolder>() {
    var items = arrayListOf<BusInfo>()

    data class BusInfo(val string: String)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationBusListViewHolder =
            StationBusListViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: StationBusListViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {

        }

    }


}

