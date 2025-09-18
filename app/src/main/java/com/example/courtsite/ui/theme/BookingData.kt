package com.example.courtsite.ui.theme

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.courtsite.data.model.Venue
import com.example.courtsite.data.model.venuesByLocation
import com.example.courtsite.data.model.VenueDetail

object BookingData {
    // Currently selected venue and sport context
    var currentVenue: Venue? = null
        private set
    var currentSport: String? = null
        private set

    fun setCurrentVenueByName(name: String) {
        currentVenue = venuesByLocation.values.flatten().firstOrNull { it.name.equals(name, ignoreCase = true) }
    }

    fun setCurrentSport(sport: String?) {
        currentSport = sport?.takeIf { it.isNotBlank() }
    }

    fun getVenueByName(name: String): Venue? =
        venuesByLocation.values.flatten().firstOrNull { it.name.equals(name, ignoreCase = true) }

    fun getVenueDetail(name: String): VenueDetail? {
        val v = getVenueByName(name) ?: return null
        return VenueDetail(
            name = v.name,
            location = v.location,
            sportType = v.sportType,
            imageRes = v.imageRes,
            pricePerHour = v.price
        )
    }

    fun getCourtsCount(name: String?): Int {
        val v = name?.let { getVenueByName(it) } ?: currentVenue
        return v?.courts ?: 0
    }

    fun getPriceLabel(name: String?): String? {
        val v = name?.let { getVenueByName(it) } ?: currentVenue
        return v?.price
    }

    fun getVenueHourlyRate(name: String? = currentVenue?.name): Double {
        val v = name?.let { getVenueByName(it) } ?: currentVenue
        val priceLabel = v?.price ?: return 40.0
        val digits = priceLabel.filter { it.isDigit() || it == '.' }
        return digits.toDoubleOrNull() ?: 40.0
    }

    // Centralized time slots (09:00 to 02:00 next day, 30-min interval)
    fun timeSlots(): List<LocalTime> {
        val result = mutableListOf<LocalTime>()
        var minutesFromStart = 0
        val totalMinutes = (17 * 60)
        val start = LocalTime.of(9, 0)
        while (minutesFromStart <= totalMinutes) {
            result.add(start.plusMinutes(minutesFromStart.toLong()))
            minutesFromStart += 30
        }
        return result
    }

    fun computePrice(start: LocalTime, end: LocalTime, venueName: String? = currentVenue?.name): Double {
        val minutes = java.time.Duration.between(start, end).toMinutes().toDouble().coerceAtLeast(0.0)
        val hours = minutes / 60.0
        val rate = getVenueHourlyRate(venueName)
        return hours * rate
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

    // Check if a slot is already booked/unavailable (delegates to BookingStore)
    fun isSlotBooked(date: LocalDate, court: Int, timeIndex: Int, times: List<LocalTime>, venueName: String? = currentVenue?.name): Boolean {
        return BookingStore.isSlotBooked(date, court, timeIndex, times, venueName)
    }

    // Check if a slot is part of any cart item (selected/committed)
    fun isInCartAt(date: LocalDate, court: Int, timeIndex: Int, times: List<LocalTime>, venueName: String? = currentVenue?.name): Boolean {
        return CartStore.items.any { item ->
            item.venueName.equals(venueName, ignoreCase = true) &&
                item.date == date &&
                item.court == court &&
                run {
                    val startIdx = times.indexOf(item.start)
                    val endIdx = times.indexOf(item.end)
                    timeIndex in startIdx until endIdx
                }
        }
    }

    // Check if a court is free for the entire [startIndex, endIndex) range
    fun isCourtAvailableRange(
        venueName: String,
        date: LocalDate,
        court: Int,
        startIndex: Int,
        endIndexExclusive: Int,
        times: List<LocalTime>
    ): Boolean {
        if (startIndex < 0 || endIndexExclusive > times.size || startIndex >= endIndexExclusive) return false
        for (idx in startIndex until endIndexExclusive) {
            if (isSlotBooked(date, court, idx, times, venueName)) return false
            if (isInCartAt(date, court, idx, times, venueName)) return false
        }
        return true
    }
}


