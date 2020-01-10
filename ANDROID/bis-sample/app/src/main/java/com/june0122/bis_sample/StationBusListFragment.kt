package com.june0122.bis_sample

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class StationBusListFragment : Fragment() {

    data class ItemList(val stationId: String, val stationName: String)

    private val itemLists = arrayListOf<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_bus_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Thread(Runnable {
            activity?.runOnUiThread {
                getXmlData()

            }
        }).start()

    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun getXmlData() {
        val serviceKey = "5K4YH8DzyYk6r4IHryQgYEXHCIOLtnuw%2BlwR3oevwZr%2F5kdBfD2N%2B%2F1OUqNUFTOuNRGjfIerFnv4yZYWNnFenQ%3D%3D"
        val busRouteId = "100100118"
//        val url: URL = URL("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll?ServiceKey=5K4YH8DzyYk6r4IHryQgYEXHCIOLtnuw%2BlwR3oevwZr%2F5kdBfD2N%2B%2F1OUqNUFTOuNRGjfIerFnv4yZYWNnFenQ%3D%3D&busRouteId=100100118")

        val url: URL = URL("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll?ServiceKey=$serviceKey&busRouteId=$busRouteId")
        val inputStream = url.openStream()
        val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()

        parser.setInput(InputStreamReader(inputStream, "UTF-8"))

        var parserEvent = parser.eventType

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when (parserEvent) {
                XmlPullParser.START_DOCUMENT -> {
                    Log.d("XXX", "[Start Document]")
                }

                XmlPullParser.START_TAG -> {
                        Log.d("XXX", parser.name)
                }

                XmlPullParser.TEXT -> {
                        Log.d("XXX", parser.text)
                }
            }
            parserEvent = parser.next()
        }
        Log.d("XXX", "[End Document]")
    }

}