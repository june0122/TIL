package com.june0122.bis_sample

import android.os.StrictMode

fun formatTime(string: String): String {
    val hour = string.substring(8, 10)
    val minute = string.substring(10, 12)

    return "$hour:$minute"
}

fun setStrictMode() {
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
}