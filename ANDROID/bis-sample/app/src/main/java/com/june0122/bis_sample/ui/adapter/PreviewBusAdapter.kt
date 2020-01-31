package com.june0122.bis_sample.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.bis_sample.model.BusData
import com.june0122.bis_sample.ui.viewholder.PreviewBusViewHolder
import kotlinx.android.synthetic.main.item_preview_bus_list.view.*

class PreviewBusAdapter : RecyclerView.Adapter<PreviewBusViewHolder>() {
    var items = arrayListOf<BusData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewBusViewHolder =
            PreviewBusViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PreviewBusViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {
            busNumberPreviewTextView.text = model.busNumber
            busTypeTextView.text = model.busType
        }

    }


}
