package com.june0122.bis_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.june0122.bis_sample.R
import com.june0122.bis_sample.model.BusList
import com.june0122.bis_sample.model.Data.Companion.SERVICE_KEY
import com.june0122.bis_sample.ui.adapter.StationBusListAdapter
import com.june0122.bis_sample.utils.createParser
import com.june0122.bis_sample.utils.setStrictMode
import kotlinx.android.synthetic.main.fragment_station_bus_list.*
import kotlinx.android.synthetic.main.layout_appbar_station_bus_list.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.net.URL

class StationBusListFragment(private val arsId : String) : Fragment() {
    private val busList = arrayListOf<BusList>()
    private val stationBusListAdapter = StationBusListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_bus_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stationBusListLayoutManager = LinearLayoutManager(context)
        busListRecyclerView.layoutManager = stationBusListLayoutManager
        stationBusListLayoutManager.orientation = LinearLayoutManager.VERTICAL
        busListRecyclerView.adapter = stationBusListAdapter

        setStrictMode()

        Thread(Runnable {
            activity?.runOnUiThread {
                busList.clear()
                searchBusListAtStation(arsId)
            }
        }).start()

        backButtonImageView.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, SearchInfoFragment())
                    ?.addToBackStack(null)?.commit()
        }

        toolbarHomeButton.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, SearchInfoFragment())
                    ?.addToBackStack(null)?.commit()
        }

    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun searchBusListAtStation(stationId: String) {
        val url = URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?ServiceKey=${SERVICE_KEY}&arsId=$stationId")

        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent

        var arrmsg1Tag = false
        var arrmsg2Tag = false
        var arsIdTag = false
        var nxtStnTag = false
        var rtNmTag = false
        var stNmTag = false

        var arrmsg1 = ""
        var arrmsg2 = ""
        var arsId = ""
        var nxtStn = ""
        var rtNm = ""
        var stNm: String

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "arrmsg1" -> {
                            arrmsg1Tag = true
                        }
                        "arrmsg2" -> {
                            arrmsg2Tag = true
                        }
                        "arsId" -> {
                            arsIdTag = true
                        }
                        "nxtStn" -> {
                            nxtStnTag = true
                        }
                        "rtNm" -> {
                            rtNmTag = true
                        }
                        "stNm" -> {
                            stNmTag = true
                        }
                    }
                }

                XmlPullParser.TEXT -> {
                    when {
                        arrmsg1Tag -> {
                            arrmsg1 = parser.text
                        }
                        arrmsg2Tag -> {
                            arrmsg2 = parser.text
                        }
                        arsIdTag -> {
                            arsId = parser.text
                        }
                        nxtStnTag -> {
                            nxtStn = parser.text
                        }
                        rtNmTag -> {
                            rtNm = parser.text
                        }
                        stNmTag -> {
                            stNm = parser.text

                            val data = BusList(arsId, stNm, rtNm, nxtStn, arrmsg1, arrmsg2)
                            busList.add(data)
                        }
                    }

                    arrmsg1Tag = false
                    arrmsg2Tag = false
                    arsIdTag = false
                    nxtStnTag = false
                    rtNmTag = false
                    stNmTag = false
                }
            }
            parserEvent = parser.next()
        }

        stationBusListAdapter.items.clear()
        stationBusListAdapter.items.addAll(busList)
        stationBusListAdapter.notifyDataSetChanged()

        stationArsIdTextView?.text = busList[0].arsId
        stationNameTextView?.text = busList[0].stationName
        stationDirectionTextView?.text = resources.getString(R.string.direction_station, busList[0].nextStation)
    }
}