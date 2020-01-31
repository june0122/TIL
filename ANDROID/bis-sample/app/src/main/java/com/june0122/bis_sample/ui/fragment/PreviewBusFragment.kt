package com.june0122.bis_sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.june0122.bis_sample.R
import com.june0122.bis_sample.model.BusData
import com.june0122.bis_sample.model.Data.Companion.SERVICE_KEY
import com.june0122.bis_sample.ui.adapter.PreviewBusAdapter
import com.june0122.bis_sample.utils.createParser
import com.june0122.bis_sample.utils.formatTime
import com.june0122.bis_sample.utils.setStrictMode
import kotlinx.android.synthetic.main.fragment_preview_bus.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.net.URL

private fun checkBusType(busTypeNumber: String): String {
    return when (busTypeNumber) {
        "0" -> "공용"
        "1" -> "공항"
        "2" -> "마을"
        "3" -> "간선"
        "4" -> "지선"
        "5" -> "순환"
        "6" -> "광역"
        "7" -> "인천"
        "8" -> "경기"
        "9" -> "폐지"
        else -> "미정"
    }
}

class PreviewBusFragment(private var inputData: String) : Fragment() {
    private val busData = arrayListOf<BusData>()
    private val previewBusAdapter = PreviewBusAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview_bus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previewBusListLayoutManager = LinearLayoutManager(context)
        previewBusRecyclerView.layoutManager = previewBusListLayoutManager
        previewBusListLayoutManager.orientation = LinearLayoutManager.VERTICAL
        previewBusRecyclerView.adapter = previewBusAdapter

        setStrictMode()

        Thread(Runnable {
            activity?.runOnUiThread {
                busData.clear()
                when (inputData) {
                    "" -> busData.clear()
                    else -> searchBusRouteId(inputData)
                }
            }
        }).start()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun searchBusRouteId(busNumber: String) {
        val url = URL("http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList?ServiceKey=$SERVICE_KEY&strSrch=$busNumber")

        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent

        var busRouteIdTag = false
        var busRouteNmTag = false
        var edStationNmTag = false
        var firstBusTmTag = false
        var lastBusTmTag = false
        var lastBusYnTag = false
        var routeTypeTag = false
        var stStationNmTag = false
        var termTag = false

        var busRouteId = ""
        var busRouteNm = ""
        var edStationNm = ""
        var firstBusTm = ""
        var lastBusTm = ""
        var lastBusYn = ""
        var routeType = ""
        var stStationNm = ""
        var term: String


        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_DOCUMENT -> {

                }

                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "busRouteId" -> {
                            busRouteIdTag = true
                        }
                        "busRouteNm" -> {
                            busRouteNmTag = true
                        }
                        "edStationNm" -> {
                            edStationNmTag = true
                        }
                        "firstBusTm" -> {
                            firstBusTmTag = true
                        }
                        "lastBusTm" -> {
                            lastBusTmTag = true
                        }
                        "lastBusYn" -> {
                            lastBusYnTag = true
                        }
                        "routeType" -> {
                            routeTypeTag = true
                        }
                        "stStationNm" -> {
                            stStationNmTag = true
                        }
                        "term" -> {
                            termTag = true
                        }

                    }
                }

                XmlPullParser.TEXT -> {
                    when {
                        busRouteIdTag -> {
                            busRouteId = parser.text
                        }
                        busRouteNmTag -> {
                            busRouteNm = parser.text
                        }
                        edStationNmTag -> {
                            edStationNm = parser.text
                        }
                        firstBusTmTag -> {
                            firstBusTm = formatTime(parser.text)
                        }
                        lastBusTmTag -> {
                            lastBusTm = formatTime(parser.text)
                        }
                        lastBusYnTag -> {
                            lastBusYn = parser.text
                        }
                        routeTypeTag -> {
                            routeType = checkBusType(parser.text)
                        }
                        stStationNmTag -> {
                            stStationNm = parser.text
                        }
                        termTag -> {
                            term = parser.text

                            val data = BusData(
                                    busRouteNm,
                                    busRouteId,
                                    routeType,
                                    term,
                                    stStationNm,
                                    edStationNm,
                                    firstBusTm,
                                    lastBusTm,
                                    lastBusYn
                            )
                            busData.add(data)
                        }
                    }

                    busRouteIdTag = false
                    busRouteNmTag = false
                    edStationNmTag = false
                    firstBusTmTag = false
                    lastBusTmTag = false
                    lastBusYnTag = false
                    routeTypeTag = false
                    stStationNmTag = false
                    termTag = false
                }
            }
            parserEvent = parser.next()
        }

        previewBusAdapter.items.clear()
        previewBusAdapter.items.addAll(busData)
        previewBusAdapter.notifyDataSetChanged()

        busData.forEach {
            Log.d(
                    "XXX",
                    "[${it.busNumber}번 버스] (${it.busId}), [버스 종류] : ${checkBusType(it.busType)}, [배차 간격] : ${it.term}분, [기점] : ${it.startStationName} ~ [종점] : ${it.endStationName}, [첫차] : ${it.firstTime} ~ [막차] : ${it.lastTime}, [막차 운행 여부] : ${it.lastBusPresence}"
            )
        }
    }
}