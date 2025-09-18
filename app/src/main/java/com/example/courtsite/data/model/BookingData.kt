package com.example.courtsite.data.model

import com.example.courtsite.ui.theme.CartStore
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object BookingData {
    fun computePrice(start: LocalTime, end: LocalTime): Double {
        val minutes = java.time.Duration.between(start, end).toMinutes().toInt().coerceAtLeast(0)
        val halfHours = minutes / 30
        // Same as Availability: base RM 40 for 1 hour (2 half-hours), +20 per extra half-hour
        return 40.0 + (halfHours - 2).coerceAtLeast(0) * 20.0
    }

    fun courtsFor(venueName: String, sportType: String, count: Int = 8): List<String> {
        return (1..count).map { "Court $it $venueName" }
    }

    fun cartTotals(): Pair<Double, Int> {
        val total = CartStore.items.sumOf { it.price }
        val count = CartStore.items.size
        return total to count
    }

    fun findCartItemIndex(date: LocalDate, start: LocalTime, end: LocalTime, court: Int): Int? {
        val fmt = DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH)
        val s = start.format(fmt)
        val e = end.format(fmt)
        return CartStore.items.indexOfFirst { it.date == date && it.start.format(fmt) == s && it.end.format(fmt) == e && it.court == court }
            .takeIf { it >= 0 }
    }
}


