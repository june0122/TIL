package com.june0122.bis_sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.june0122.bis_sample.utils.formatTime
import com.june0122.bis_sample.utils.setStrictMode
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class StationBusListFragment : Fragment() {

    data class ItemList(val stationName: String, val stationId: String, val firstTime: String, val lastTime: String)

    private val itemLists = arrayListOf<ItemList>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_bus_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStrictMode()

        Thread(Runnable {
            activity?.runOnUiThread {
                getXmlData()
            }
        }).start()
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun getXmlData() {
        val serviceKey = "6Gi1UHlRZK0oxUZHrb5I5L%2Fb466WpwHkOp%2BBfVMdZFJAq6O7B5E1uQuxNlgAbfxrjjDSTJOuyGjrU25iiZS6hA%3D%3D"
        val busRouteId = "100100118"  // 버스 노선 ID
        val url = URL("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll?ServiceKey=$serviceKey&busRouteId=$busRouteId")

        val inputStream = url.openStream()
        val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()

        parser.setInput(InputStreamReader(inputStream, "UTF-8"))

        var parserEvent = parser.eventType

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

                            val data = ItemList(stationName, stationId, firstTime, lastTime)
                            itemLists.add(data)
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


        itemLists.forEach {
            Log.d("XXX", "${it.stationName} (${it.stationId}) / 첫차 ${it.firstTime} ~ 막차 ${it.lastTime}")
        }

    }
}