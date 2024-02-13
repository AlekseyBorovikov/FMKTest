package com.example.fmk.utils.extensions

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

const val DATE_AND_TIME_TO_MILLIS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
const val SECOND_IN_MILLIS = 1000L
const val MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS
const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS
const val WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS
const val MONTH_IN_MILLIS = 30 * DAY_IN_MILLIS
const val YEAR_IN_MILLIS = 12 * MONTH_IN_MILLIS

fun Long.utcToCalendar(locale: Locale? = null): Calendar =
    Calendar
        .getInstance(locale)
        .apply {
            timeZone = TimeZone.getTimeZone("UTC")
            timeInMillis = this@utcToCalendar
        }

val Calendar.hour get() = this.get(Calendar.HOUR_OF_DAY)
val Calendar.minute get() = this.get(Calendar.MINUTE)
val Calendar.second get() = this.get(Calendar.SECOND)
val Calendar.year get() = this.get(Calendar.YEAR)
val Calendar.month get() = this.get(Calendar.MONTH)
val Calendar.dayOfMonth get() = this.get(Calendar.DAY_OF_MONTH)
val Calendar.utc get() = this.apply { timeZone = TimeZone.getTimeZone("UTC") }