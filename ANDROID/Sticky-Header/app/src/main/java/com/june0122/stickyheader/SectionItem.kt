package com.june0122.stickyheader

import com.june0122.stickyheader.Data.BusInfo
import com.june0122.stickyheader.Section.Companion.ITEM

class SectionItem(private val itemSection: Int, private val data : BusInfo) : Section {
    override fun type(): Int {
        return ITEM
    }

    override fun data(): BusInfo {
        return data
    }

    override fun sectionPosition(): Int {
        return itemSection
    }
}