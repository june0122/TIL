package com.june0122.stickyheader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.june0122.stickyheader.Data.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stickyRecyclerView.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        stickyRecyclerView.addItemDecoration(dividerItemDecoration)

        val adapter = SectionAdapter()
        stickyRecyclerView.adapter = adapter

        val decorator = StickyHeaderItemDecorator(adapter, stickyRecyclerView)
        decorator.attachToRecyclerView(stickyRecyclerView)


        val seoulBusInfoSet = arrayListOf(
                BusInfo("753", "간선"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("24", "마을"),
                BusInfo("100-2", "순환"),
                BusInfo("강남 01", "마을")

        )

        val gyeonggiBusInfoSet = arrayListOf(
                BusInfo("720-2", "광주시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("7200", "공항")

        )


        val incheonBusInfoSet = arrayListOf(
                BusInfo("720-2", "광주시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("202", "성남시 일반"),
                BusInfo("7200", "공항")
        )

        adapter.addBusData(0, seoulBusInfoSet)
        adapter.addBusData(1, gyeonggiBusInfoSet)
        adapter.addBusData(2, incheonBusInfoSet)

        adapter.notifyDataSetChanged()
    }


    private fun SectionAdapter.addBusData(location: Int, dataSet: ArrayList<BusInfo>) {
        this.items.add(SectionHeader(location, BusInfo("", "")))
        for (i in 0 until dataSet.size) {
            this.items.add(SectionItem(location, dataSet[i]))
        }
    }
}
