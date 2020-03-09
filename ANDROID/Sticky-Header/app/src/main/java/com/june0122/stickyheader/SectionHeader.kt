package com.june0122.stickyheader

import com.june0122.stickyheader.Data.BusInfo
import com.june0122.stickyheader.Section.Companion.HEADER

class SectionHeader(private val headerSection: Int, private val data: BusInfo) : Section {
    override fun type(): Int {
        return HEADER
    }

    override fun data(): BusInfo {
        return data
    }

    override fun sectionPosition(): Int {
        return headerSection
    }
}