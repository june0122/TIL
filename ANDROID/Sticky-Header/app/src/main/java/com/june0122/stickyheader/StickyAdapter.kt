package com.june0122.stickyheader

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class StickyAdapter<SVH : RecyclerView.ViewHolder?, VH : RecyclerView.ViewHolder?> :
    RecyclerView.Adapter<VH>() {

    abstract fun getHeaderPositionForItem(itemPosition: Int): Int

    abstract fun onBindHeaderViewHolder(holder: SVH, headerPosition: Int)

    abstract fun onCreateHeaderViewHolder(parent: ViewGroup): SVH
}