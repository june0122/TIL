package com.june0122.bis_sample.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.bis_sample.ui.fragment.BusData
import com.june0122.bis_sample.ui.viewholder.SearchViewHolder
import kotlinx.android.synthetic.main.item_search_bus_list.view.*

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {
    var items = arrayListOf<BusData>()

//    data class BusInfo(val string: String)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
            SearchViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {
            busNumberTextView.text = model.busNumber
            busTypeTextView.text = model.busType
        }

    }


}
