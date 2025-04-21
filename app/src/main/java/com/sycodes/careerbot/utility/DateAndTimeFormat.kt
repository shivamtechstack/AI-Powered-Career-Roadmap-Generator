package com.sycodes.careerbot.utility

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateAndTimeFormat {
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}