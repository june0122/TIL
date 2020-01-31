package com.june0122.bis_sample.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.bis_sample.model.BusData
import com.june0122.bis_sample.model.StationPreviewData
import com.june0122.bis_sample.ui.viewholder.PreviewBusViewHolder
import com.june0122.bis_sample.ui.viewholder.PreviewStationViewHolder
import kotlinx.android.synthetic.main.item_preview_bus_list.view.*
import kotlinx.android.synthetic.main.item_preview_station_list.view.*

class PreviewStationAdapter : RecyclerView.Adapter<PreviewStationViewHolder>() {
    var items = arrayListOf<StationPreviewData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewStationViewHolder =
            PreviewStationViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PreviewStationViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {
            stationNamePreviewTextView.text = model.stationName
            stationArsIdPreviewTextView.text = model.stationArsId
        }

    }


}
