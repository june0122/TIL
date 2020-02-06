package com.june0122.bis_sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.june0122.bis_sample.R
import com.june0122.bis_sample.model.Data.Companion.SERVICE_KEY
import com.june0122.bis_sample.model.RouteData
import com.june0122.bis_sample.model.StationList
import com.june0122.bis_sample.ui.adapter.BusRouteAdapter
import com.june0122.bis_sample.utils.checkBusType
import com.june0122.bis_sample.utils.createParser
import com.june0122.bis_sample.utils.formatTime
import com.june0122.bis_sample.utils.setStrictMode
import kotlinx.android.synthetic.main.fragment_bus_route.*
import kotlinx.android.synthetic.main.layout_appbar_bus_route.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.net.URL

class BusRouteFragment(private var inputData: String) : Fragment() {
    private val routeData = arrayListOf<RouteData>()
    private val stationList = arrayListOf<StationList>()
    private val busRouteAdapter = BusRouteAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bus_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val busRouteLayoutManager = LinearLayoutManager(context)
        busRouteRecyclerView.layoutManager = busRouteLayoutManager
        busRouteLayoutManager.orientation = LinearLayoutManager.VERTICAL
        busRouteRecyclerView.adapter = busRouteAdapter

        setStrictMode()

        Thread(Runnable {
            activity?.runOnUiThread {

                stationList.clear()
                when (inputData) {
                    "" -> stationList.clear()
                    else -> {
                        searchBusRouteInfo(inputData)
                        searchBusRoute(searchBusRouteInfo(inputData))
                    }
                }
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
    fun searchBusRoute(routeId: String) {
        val url = URL("http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?ServiceKey=$SERVICE_KEY&busRouteId=$routeId")

        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent

        var arsIdTag = false
        var beginTmTag = false
        var busRouteIdTag = false
        var busRouteNmTag = false
        var directionTag = false
        var gpsXTag = false
        var gpsYTag = false
        var lastTmTag = false
        var posXTag = false
        var posYTag = false
        var routeTypeTag = false
        var sectSpdTag = false
        var sectionTag = false
        var seqTag = false
        var stationTag = false
        var stationNmTag = false
        var stationNoTag = false
        var transYnTag = false
        var fullSectDistTag = false
        var trnstnidTag = false

        var arsId = ""
        var beginTm = ""
        var busRouteId = ""
        var busRouteNm = ""
        var direction = ""
        var gpsX = ""
        var gpsY = ""
        var lastTm = ""
        var posX = ""
        var posY = ""
        var routeType = ""
        var sectSpd = ""
        var section = ""
        var seq = ""
        var station = ""
        var stationNm = ""
        var stationNo = ""
        var transYn = ""
        var fullSectDist = ""
        var trnstnid = ""

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "arsId" -> {
                            arsIdTag = true
                        }
                        "beginTm" -> {
                            beginTmTag = true
                        }
                        "busRouteId" -> {
                            busRouteIdTag = true
                        }
                        "busRouteNm" -> {
                            busRouteNmTag = true
                        }
                        "direction" -> {
                            directionTag = true
                        }
                        "gpsX" -> {
                            gpsXTag = true
                        }
                        "gpsY" -> {
                            gpsYTag = true
                        }
                        "lastTm" -> {
                            lastTmTag = true
                        }
                        "posX" -> {
                            posXTag = true
                        }
                        "posY" -> {
                            posYTag = true
                        }
                        "routeType" -> {
                            routeTypeTag = true
                        }
                        "sectSpd" -> {
                            sectSpdTag = true
                        }
                        "section" -> {
                            sectionTag = true
                        }
                        "seq" -> {
                            seqTag = true
                        }
                        "station" -> {
                            stationTag = true
                        }
                        "stationNm" -> {
                            stationNmTag = true
                        }
                        "stationNo" -> {
                            stationNoTag = true
                        }
                        "transYn" -> {
                            transYnTag = true
                        }
                        "fullSectDist" -> {
                            fullSectDistTag = true
                        }
                        "trnstnid" -> {
                            trnstnidTag = true
                        }


                    }
                }

                XmlPullParser.TEXT -> {
                    when {
                        arsIdTag -> {
                            arsId = parser.text
                        }
                        beginTmTag -> {
                            beginTm = parser.text
                        }
                        busRouteIdTag -> {
                            busRouteId = parser.text
                        }
                        busRouteNmTag -> {
                            busRouteNm = parser.text
                        }
                        directionTag -> {
                            direction = parser.text
                        }
                        gpsXTag -> {
                            gpsX = parser.text
                        }
                        gpsYTag -> {
                            gpsY = parser.text
                        }
                        lastTmTag -> {
                            lastTm = parser.text
                        }
                        posXTag -> {
                            posX = parser.text
                        }
                        posYTag -> {
                            posY = parser.text
                        }
                        routeTypeTag -> {
                            routeType = parser.text
                        }
                        sectSpdTag -> {
                            sectSpd = parser.text
                        }
                        sectionTag -> {
                            section = parser.text
                        }
                        seqTag -> {
                            seq = parser.text
                        }
                        stationTag -> {
                            station = parser.text
                        }
                        stationNmTag -> {
                            stationNm = parser.text
                        }
                        stationNoTag -> {
                            stationNo = parser.text
                        }
                        transYnTag -> {
                            transYn = parser.text
                        }
                        fullSectDistTag -> {
                            fullSectDist = parser.text
                        }

                        trnstnidTag -> {
                            trnstnid = parser.text

                            val data = StationList(busRouteId, busRouteNm, stationNm, arsId, beginTm, lastTm, routeType)
                            stationList.add(data)
                        }
                    }

                    arsIdTag = false
                    beginTmTag = false
                    busRouteIdTag = false
                    busRouteNmTag = false
                    directionTag = false
                    gpsXTag = false
                    gpsYTag = false
                    lastTmTag = false
                    posXTag = false
                    posYTag = false
                    routeTypeTag = false
                    sectSpdTag = false
                    sectionTag = false
                    seqTag = false
                    stationTag = false
                    stationNmTag = false
                    stationNoTag = false
                    transYnTag = false
                    fullSectDistTag = false
                    trnstnidTag = false
                }
            }
            parserEvent = parser.next()
        }

        busRouteAdapter.items.clear()
        busRouteAdapter.items.addAll(stationList)
        busRouteAdapter.notifyDataSetChanged()

        stationList.forEach {
            Log.d("XXX", "${it.stationName} ${it.stationId} | ${it.firstTime} ~ ${it.lastTime}")
            Log.d("XXX", "$direction $gpsX $gpsY $lastTm $posX $posY $routeType $sectSpd $section $seq $station $stationNo $transYn $fullSectDist $trnstnid")
        }

    }


    @Throws(XmlPullParserException::class, IOException::class)
    fun searchBusRouteInfo(busNumber: String): String {
        val url = URL("http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList?ServiceKey=$SERVICE_KEY&strSrch=$busNumber")

        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent

        var busRouteIdTag = false
        var busRouteNmTag = false
        var corpNmTag = false
        var edStationNmTag = false
        var firstBusTmTag = false
        var lastBusTmTag = false
        var lastBusYnTag = false
        var routeTypeTag = false
        var stStationNmTag = false
        var termTag = false

        var busRouteId = ""
        var busRouteNm = ""
        var corpNm = ""
        var edStationNm = ""
        var firstBusTm = ""
        var lastBusTm = ""
        var lastBusYn = ""
        var routeType = ""
        var stStationNm = ""
        var term: String


        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "busRouteId" -> {
                            busRouteIdTag = true
                        }
                        "busRouteNm" -> {
                            busRouteNmTag = true
                        }
                        "corpNm" -> {
                            corpNmTag = true
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
                        corpNmTag -> {
                            corpNm = parser.text
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
                            routeType = parser.text
                        }
                        stStationNmTag -> {
                            stStationNm = parser.text
                        }
                        termTag -> {
                            term = parser.text

                            val data = RouteData(busRouteNm, busRouteId, routeType, term, stStationNm, edStationNm, firstBusTm, lastBusTm, lastBusYn, corpNm)
                            routeData.add(data)
                        }
                    }

                    busRouteIdTag = false
                    busRouteNmTag = false
                    corpNmTag = false
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

        Log.d("XXX", "$stStationNm ~ $edStationNm")

        busTypeTextView.text = checkBusType(routeType)
        busNumberTextView.text = busRouteNm
        firstStationNameTextView.text = stStationNm
        endStationNameTextView.text = edStationNm

        return busRouteId
    }
}
