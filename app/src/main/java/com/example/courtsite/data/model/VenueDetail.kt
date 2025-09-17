package com.example.courtsite.data.model

import androidx.annotation.DrawableRes

data class VenueDetail(
    val name: String,
    val location: String,
    val sportType: String,
    @DrawableRes val imageRes: Int,
    val rating: Double? = null,
    val pricePerHour: String? = null,
    val facilities: List<String>? = null,
    val openingHours: String? = null,
    val phone: String? = null,
    val address: String? = null
)

