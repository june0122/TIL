package com.june0122.bis_sample.model

import org.xmlpull.v1.XmlPullParser

data class ParserElement(val parser: XmlPullParser, val parserEvent: Int)

data class StationList(val stationId: String, val stationName: String)

data class BusRouteMap(
        val stationName: String,
        val stationId: String,
        val firstTime: String,
        val lastTime: String
)

data class BusList(
        val busNumber: String,
        val nextStation: String,
        val firstArrivalBusInfo: String,
        val secondArrivalBusInfo: String
)

data class BusData(
        val busNumber: String,
        val busId: String,
        val busType: String,
        val term: String,
        val startStationName: String,
        val endStationName: String,
        val firstTime: String,
        val lastTime: String,
        val lastBusPresence: String
)

data class StationPreviewData(
        val stationName: String,
        val stationArsId: String,
        val stationId: String,
        val wgs84X: String,
        val wgs84Y: String,
        val grs80X: String,
        val grs80Y: String
)

class Data {
    companion object {
        const val SERVICE_KEY = "6Gi1UHlRZK0oxUZHrb5I5L%2Fb466WpwHkOp%2BBfVMdZFJAq6O7B5E1uQuxNlgAbfxrjjDSTJOuyGjrU25iiZS6hA%3D%3D"
    }
}