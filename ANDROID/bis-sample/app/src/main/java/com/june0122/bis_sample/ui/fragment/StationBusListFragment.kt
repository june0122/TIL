package com.june0122.bis_sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.june0122.bis_sample.R
import com.june0122.bis_sample.utils.formatArrivalTime
import com.june0122.bis_sample.utils.formatTime
import com.june0122.bis_sample.utils.setStrictMode
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

const val SERVICE_KEY = "6Gi1UHlRZK0oxUZHrb5I5L%2Fb466WpwHkOp%2BBfVMdZFJAq6O7B5E1uQuxNlgAbfxrjjDSTJOuyGjrU25iiZS6hA%3D%3D"

data class ParserElement(val parser: XmlPullParser, val parserEvent: Int)

data class StationList(val stationId: String, val stationName: String)

data class BusRouteMap(val stationName: String, val stationId: String, val firstTime: String, val lastTime: String)

data class BusList(val busNumber: String, val nextStation: String, val firstArrivalBusInfo: String, val secondArrivalBusInfo: String)

data class BusData(val busNumber: String,
                   val busId: String,
                   val busType: String,
                   val term: String,
                   val startStationName: String,
                   val endStationName: String,
                   val firstTime: String,
                   val lastTime: String,
                   val lastBusPresence: String
)

class StationBusListFragment : Fragment() {
    private val stationList = arrayListOf<StationList>()
    private val busRouteMap = arrayListOf<BusRouteMap>()
    private val busList = arrayListOf<BusList>()
    private val busData = arrayListOf<BusData>()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_bus_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stationId = 14226  // 버스 정류소 ID
        val busNumber = 753  // 버스 번호
        val busRouteId = 100100118  // 버스 노선 ID
        val busStationName = "강남"  // 버스 정류소 이름

        setStrictMode()

        Thread(Runnable {
            activity?.runOnUiThread {
//                searchBusRouteId(busNumber)
                searchBusListAtStation(stationId)
//                searchStationId(busStationName)
//                searchBusRouteMap(busRouteId)
            }
        }).start()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun searchStationId(busStationName: String) {
        val url = URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByName?ServiceKey=$SERVICE_KEY&stSrch=$busStationName"
        )

        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent

        var arsIdTag = false
        var stNmTag = false

        var stationId = ""
        var stationName = ""

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "arsId" -> {
                            arsIdTag = true
                        }

                        "stNm" -> {
                            stNmTag = true
                        }
                    }
                }

                XmlPullParser.TEXT -> {
                    when {
                        arsIdTag -> {
                            stationId = parser.text
                        }

                        stNmTag -> {
                            stationName = parser.text

                            val data = StationList(stationId, stationName)
                            stationList.add(data)
                        }
                    }
                    arsIdTag = false
                    stNmTag = false
                }
            }
            parserEvent = parser.next()
        }

        stationList.forEach {
            Log.d("XXX", "${it.stationName} (${it.stationId})")
        }
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun searchBusRouteMap(busRouteId: Int) {
        val url = URL("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll?ServiceKey=$SERVICE_KEY&busRouteId=$busRouteId")

        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent

        var arrmsg1Tag = false
        var arrmsg2Tag = false
        var firstTimeTag = false
        var lastTimeTag = false
        var stationIdTag = false
        var stationNameTag = false

        var stationId = ""
        var firstTime = ""
        var lastTime = ""
        var stationName: String

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_DOCUMENT -> {
                    Log.d("XXX", "[Start Document]")
                }

                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "arrmsg1" -> {
                            arrmsg1Tag = true
                        }
                        "arrmsg2" -> {
                            arrmsg2Tag = true
                        }
                        "arsId" -> {
                            stationIdTag = true
                        }
                        "firstTm" -> {
                            firstTimeTag = true
                        }
                        "lastTm" -> {
                            lastTimeTag = true
                        }
                        "stNm" -> {
                            stationNameTag = true
                        }
                    }
                }

                XmlPullParser.TEXT -> {
                    when {
                        arrmsg1Tag -> {
                            Log.d("XXX-arrmsg1", parser.text)
                        }
                        arrmsg2Tag -> {
                            Log.d("XXX-arrmsg2", parser.text)
                        }
                        stationIdTag -> {
                            stationId = parser.text
                            Log.d("XXX-arsId", parser.text)
                        }
                        firstTimeTag -> {
                            firstTime = formatTime(parser.text)
                            Log.d("XXX-firstTm", formatTime(parser.text))
                        }
                        lastTimeTag -> {
                            lastTime = formatTime(parser.text)
                            Log.d("XXX-lastTm", formatTime(parser.text))
                        }
                        stationNameTag -> {
                            stationName = parser.text
                            Log.d("XXX-stNm", parser.text)

                            val data = BusRouteMap(stationName, stationId, firstTime, lastTime)
                            busRouteMap.add(data)
                        }
                    }

                    arrmsg1Tag = false
                    arrmsg2Tag = false
                    firstTimeTag = false
                    lastTimeTag = false
                    stationIdTag = false
                    stationNameTag = false
                }
            }
            parserEvent = parser.next()
        }

        Log.d("XXX", "[End Document]")


        busRouteMap.forEach {
            Log.d("XXX", "${it.stationName} (${it.stationId}) / 첫차 ${it.firstTime} ~ 막차 ${it.lastTime}")
        }

    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun searchBusListAtStation(stationId: Int) {
        val url = URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?ServiceKey=$SERVICE_KEY&arsId=$stationId")


        val parser = createParser(url).parser
        var parserEvent = createParser(url).parserEvent


        var arrmsg1Tag = false
        var arrmsg2Tag = false
        var nxtStnTag = false
        var rtNmTag = false

        var arrmsg1 = ""
        var arrmsg2 = ""
        var nxtStn = ""
        var rtNm = ""

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_DOCUMENT -> {

                }

                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "arrmsg1" -> {
                            arrmsg1Tag = true
                        }
                        "arrmsg2" -> {
                            arrmsg2Tag = true
                        }
                        "nxtStn" -> {
                            nxtStnTag = true
                        }
                        "rtNm" -> {
                            rtNmTag = true
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
                        nxtStnTag -> {
                            nxtStn = parser.text
                        }
                        rtNmTag -> {
                            rtNm = parser.text
                            val data = BusList(rtNm, nxtStn, arrmsg1, arrmsg2)
                            busList.add(data)
                        }
                    }

                    arrmsg1Tag = false
                    arrmsg2Tag = false
                    nxtStnTag = false
                    rtNmTag = false
                }
            }
            parserEvent = parser.next()
        }

        busList.forEach {
            Log.d("XXX", "${it.busNumber} (${it.nextStation} 뱡향) / ${it.firstArrivalBusInfo}, ${it.secondArrivalBusInfo}")
        }

        busList.forEach {
            if (formatArrivalTime(it.firstArrivalBusInfo).size == 1) {
                Log.d("XXX", formatArrivalTime(it.firstArrivalBusInfo)[0])
            } else {

                val minute = formatArrivalTime(it.firstArrivalBusInfo)[0]
                val second = formatArrivalTime(it.firstArrivalBusInfo)[1]
                val arrivalCount = formatArrivalTime(it.firstArrivalBusInfo)[2]

                Log.d("XXX", "${minute}분 ${second}초 [$arrivalCount] ")
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun searchBusRouteId(busNumber: Int) {
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
        var term = ""




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
                            routeType = parser.text
                        }
                        stStationNmTag -> {
                            stStationNm = parser.text
                        }
                        termTag -> {
                            term = parser.text

                            val data = BusData(busRouteNm, busRouteId, routeType, term, stStationNm, edStationNm, firstBusTm, lastBusTm, lastBusYn)
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

        busData.forEach {
            Log.d("XXX", "[${it.busNumber}번 버스] (${it.busId}), [버스 종류] : ${checkBusType(it.busType)}, [배차 간격] : ${it.term}분, [기점] : ${it.startStationName} ~ [종점] : ${it.endStationName}, [첫차] : ${it.firstTime} ~ [막차] : ${it.lastTime}, [막차 운행 여부] : ${it.lastBusPresence}")
        }
    }
}

private fun createParser(url: URL): ParserElement {
    val inputStream = url.openStream()
    val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()
    parser.setInput(InputStreamReader(inputStream, "UTF-8"))

    return ParserElement(parser, parser.eventType)
}

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