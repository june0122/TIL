package com.june0122.bis_sample.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.june0122.bis_sample.R
import com.june0122.bis_sample.model.BusList
import com.june0122.bis_sample.ui.viewholder.StationBusListViewHolder
import com.june0122.bis_sample.utils.formatArrivalTime
import kotlinx.android.synthetic.main.item_station_bus_list.view.*

class StationBusListAdapter : RecyclerView.Adapter<StationBusListViewHolder>() {
    var items = arrayListOf<BusList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationBusListViewHolder =
            StationBusListViewHolder(parent)

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: StationBusListViewHolder, position: Int) {
        val model = items[position]

        // ETA : Estimated time of arrival
        fun setArrivalTime(timeData: String, etaTextView: TextView, arrivalCountTextView: TextView) {
            when (formatArrivalTime(timeData).size) {
                1 -> {
                    etaTextView.text = timeData
                    arrivalCountTextView.visibility = View.GONE
                }

                else -> {
                    val minute = formatArrivalTime(timeData)[0]
                    val second = formatArrivalTime(timeData)[1]
                    val arrivalCount = formatArrivalTime(timeData)[2]

                    etaTextView.text = holder.itemView.resources.getString(R.string.estimated_time_of_arrival, minute, second)
                    arrivalCountTextView.visibility = View.VISIBLE
                    arrivalCountTextView.text = arrivalCount

                    holder.itemView.resources.getString(R.string.estimated_time_of_arrival, minute, second)
                }
            }
        }

        with(holder.itemView) {
            routeNumberTextView.text = model.busNumber
            busDirectionTextView.text = resources.getString(R.string.direction_bus, model.nextStation)
            setArrivalTime(model.firstArrivalBusInfo, firstEtaTextView, firstArrivalCountTextView)
            setArrivalTime(model.secondArrivalBusInfo, secondEtaTextView, secondArrivalCountTextView)
        }
    }
}