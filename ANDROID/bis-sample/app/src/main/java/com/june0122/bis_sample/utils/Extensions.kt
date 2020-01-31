package com.june0122.bis_sample.utils

import android.os.StrictMode
import com.june0122.bis_sample.model.ParserElement
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader
import java.net.URL

fun createParser(url: URL): ParserElement {
    val inputStream = url.openStream()
    val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()
    parser.setInput(InputStreamReader(inputStream, "UTF-8"))

    return ParserElement(parser, parser.eventType)
}

fun formatTime(string: String): String {
    val hour = string.substring(8, 10)
    val minute = string.substring(10, 12)

    return "$hour:$minute"
}

fun formatArrivalTime(string: String): ArrayList<String> {

    return if (string.contains("분") && string.contains("초")) {

        val minuteIndex = string.indexOf("분")
        val secondIndex = string.indexOf("초")
        val leftBracketIndex = string.indexOf("[")
        val rightBracketIndex = string.indexOf("]")

        val minute = string.substring(0, minuteIndex)
        val second = string.substring(minuteIndex + 1, secondIndex)
        val arrivalCount = string.substring(leftBracketIndex + 1, rightBracketIndex).replace(" ", "")

        arrayListOf(minute, second, arrivalCount)

    } else if (string.contains("분")) {

        val minuteIndex = string.indexOf("분")
        val leftBracketIndex = string.indexOf("[")
        val rightBracketIndex = string.indexOf("]")

        val minute = string.substring(0, minuteIndex)
        val second = "0"
        val arrivalCount = string.substring(leftBracketIndex + 1, rightBracketIndex).replace(" ", "")

        arrayListOf(minute, second, arrivalCount)

    } else {
        arrayListOf(string)
    }
}

fun setStrictMode() {
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
}