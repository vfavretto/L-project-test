package com.itinerary.core.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
    private const val DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm"
    private const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun formatDate(timestamp: Long, pattern: String = DEFAULT_DATE_FORMAT): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDateTime(timestamp: Long): String {
        return formatDate(timestamp, DEFAULT_DATETIME_FORMAT)
    }

    fun parseDate(dateString: String, pattern: String = DEFAULT_DATE_FORMAT): Long? {
        return try {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentTimestamp(): Long = System.currentTimeMillis()

    fun isToday(timestamp: Long): Boolean {
        val today = Date()
        val date = Date(timestamp)
        return isSameDay(today, date)
    }

    fun isFuture(timestamp: Long): Boolean {
        return timestamp > getCurrentTimestamp()
    }

    fun isPast(timestamp: Long): Boolean {
        return timestamp < getCurrentTimestamp()
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = java.util.Calendar.getInstance().apply { time = date1 }
        val cal2 = java.util.Calendar.getInstance().apply { time = date2 }
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }

    fun getDaysDifference(timestamp1: Long, timestamp2: Long): Long {
        val diff = timestamp2 - timestamp1
        return diff / (1000 * 60 * 60 * 24)
    }
}
