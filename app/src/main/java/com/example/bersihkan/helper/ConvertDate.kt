package com.example.bersihkan.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertToDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault())

    val date = inputFormat.parse(dateString) ?: ""
    return outputFormat.format(date)
}