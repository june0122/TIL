package com.june0122.stickyheader

import com.june0122.stickyheader.Data.*

interface Section {
    fun type(): Int
    fun data(): BusInfo
    fun sectionPosition(): Int

    companion object {
        const val HEADER = 0
        const val ITEM = 1
        const val CUSTOM_HEADER = 2
    }
}