package com.example.courtsite.data.model

import com.example.courtsite.R

data class Venue(
    val id: String,
    val name: String,
    val location: String,
    val sportType: String,
    val imageRes: Int,
    val address: String = "",
    val amenities: List<String> = emptyList(),
    val courts: Int = 0,
    val price: String = "",
    val specialOffer: String = ""
)

// Sample venues data - accessible from both files
val venuesByLocation = mapOf(
    "Ara Damansara, Selangor" to listOf(
        Venue(
            id = "1",
            name = "Pickle Royal",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara1,
            address = "Jalan PJU 1A/46, Ara Damansara, 47301 Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Food", "Drinks", "Toilets", "Showers"),
            courts = 6,
            price = "RM35/hr",
            specialOffer = "Weekend Special: RM30/hr"
        ),
        Venue(
            id = "2",
            name = "Time to Tennis @ Citta Mall",
            location = "Petaling Jaya, Selangor",
            sportType = "TENNIS, PICKLEBALL",
            imageRes = R.drawable.ara2,
            address = "Citta Mall, Jalan PJU 1A/48, Ara Damansara, 47301 Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Food Court", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM40/hr"
        ),
        Venue(
            id = "3",
            name = "NZX Pickleball Club & Sports Arena",
            location = "Petaling Jaya, Selangor",
            sportType = "BADMINTON, PICKLEBALL",
            imageRes = R.drawable.ara3,
            address = "NZX Sports Arena, Ara Damansara, Petaling Jaya",
            amenities = listOf("Parking", "Cafe", "Lockers", "Showers", "Pro Shop"),
            courts = 8,
            price = "RM45/hr",
            specialOffer = "Student Discount: 20% off"
        )

    ),
    "Petaling Jaya" to listOf(
        Venue(
            id = "4",
            name = "JC Pickleball Centre @ PJ SS2",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ic_launcher_foreground,
            address = "SS2, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Toilets", "Drinks"),
            courts = 4,
            price = "RM32/hr"
        )
    ),
    "Kuala Lumpur" to listOf(
        Venue(
            id = "5",
            name = "KL Sports Center",
            location = "Kuala Lumpur",
            sportType = "MULTI-SPORT",
            imageRes = R.drawable.ic_launcher_foreground,
            address = "Kuala Lumpur City Center",
            amenities = listOf("Parking", "Food", "Drinks", "Toilets", "Showers", "Lockers"),
            courts = 10,
            price = "RM50/hr"
        )
    ),
    "Shah Alam" to listOf(
        Venue(
            id = "6",
            name = "Shah Alam Sports Complex",
            location = "Shah Alam, Selangor",
            sportType = "MULTI-SPORT",
            imageRes = R.drawable.ic_launcher_foreground,
            address = "Shah Alam, Selangor",
            amenities = listOf("Parking", "Food", "Drinks", "Toilets", "Showers", "Surau"),
            courts = 12,
            price = "RM45/hr"
        )
    ),
    "Ampang, Selangor" to listOf(
        Venue(
            id = "7",
            name = "P'ker Park",
            location = "Ampang, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ic_launcher_foreground,
            address = "LOT 9658 JALAN DAGANG B/3A TAMAN DAGANG 68000 AMPANG SELANGOR MALAYSIA",
            amenities = listOf("Parking", "Food", "Drinks", "Surau", "Toilets"),
            courts = 6,
            price = "RM32/hr",
            specialOffer = "Sunday Family Day RM32/hr Special"
        )
    )
)