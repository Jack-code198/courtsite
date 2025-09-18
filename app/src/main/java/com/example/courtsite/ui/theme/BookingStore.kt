package com.example.courtsite.ui.theme

import java.time.LocalDate
import java.time.LocalTime

// Store for managing booked slots to mark them as unavailable
object BookingStore {
    private val bookedSlots = mutableSetOf<BookedSlot>()

    data class BookedSlot(
        val venueName: String,
        val date: LocalDate,
        val court: Int,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val sport: String
    )

    fun markBookingsAsUnavailable(cartItems: List<CartItem>) {
        cartItems.forEach { item ->
            bookedSlots.add(
                BookedSlot(
                    venueName = item.venueName,
                    date = item.date,
                    court = item.court,
                    startTime = item.start,
                    endTime = item.end,
                    sport = item.sport
                )
            )
        }
    }

    fun isSlotBooked(date: LocalDate, court: Int, timeIndex: Int, times: List<LocalTime>, venueName: String? = BookingData.currentVenue?.name): Boolean {
        val time = times[timeIndex]
        return bookedSlots.any { booked ->
            booked.venueName.equals(venueName, ignoreCase = true) &&
            booked.date == date &&
            booked.court == court &&
            time >= booked.startTime &&
            time < booked.endTime
        }
    }

    fun getAllBookedSlots(): Set<BookedSlot> = bookedSlots.toSet()
}
