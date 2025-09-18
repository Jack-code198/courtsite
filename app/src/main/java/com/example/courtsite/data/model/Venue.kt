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
    "Ampang, Selangor" to listOf(
        Venue(
            id = "ap1",
            name = "P'ker Park",
            location = "Ampang, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ap1,
            address = "LOT 9658 JALAN DAGANG B/3A TAMAN DAGANG 68000 AMPANG SELANGOR MALAYSIA",
            amenities = listOf("Parking", "Food", "Drinks", "Surau", "Toilets"),
            courts = 6,
            price = "RM32/hr",
            specialOffer = "Sunday Family Day RM32/hr Special"
        ),
        Venue(
            id = "ap2",
            name = "Futsalhub & Badmintonhub @ Ampang",
            location = "Ampang, Selangor",
            sportType = "FUTSAL, BADMINTON",
            imageRes = R.drawable.ap2,
            address = "Ampang, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 8,
            price = "RM35/hr"
        ),
        Venue(
            id = "ap3",
            name = "Ampang Badminton Centre",
            location = "Ampang, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.ap3,
            address = "Ampang, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 10,
            price = "RM30/hr"
        ),
        Venue(
            id = "ap4",
            name = "Pickle Power Ampang",
            location = "Ampang, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ap4,
            address = "Ampang, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM36/hr"
        )
    ),
    "Ara Damansara, Selangor" to listOf(
        Venue(
            id = "ara1",
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
            id = "ara2",
            name = "Time to Tennis @ Citta Mall",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL, TENNIS",
            imageRes = R.drawable.ara2,
            address = "Citta Mall, Jalan PJU 1A/48, Ara Damansara, 47301 Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Food Court", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM40/hr"
        ),
        Venue(
            id = "ara3",
            name = "NZX Pickleball Club & Sports Arena",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL, BADMINTON",
            imageRes = R.drawable.ara3,
            address = "NZX Sports Arena, Ara Damansara, Petaling Jaya",
            amenities = listOf("Parking", "Cafe", "Lockers", "Showers", "Pro Shop"),
            courts = 8,
            price = "RM45/hr",
            specialOffer = "Student Discount: 20% off"
        ),
        Venue(
            id = "ara4",
            name = "RKS Sports Centre (Ara Damansara)",
            location = "Petaling Jaya, Selangor",
            sportType = "FUTSAL, SEPAK TAKRAW, DODGEBALL, HANDBALL, PICKLEBALL, TEQBALL, BADMINTON, MARTIAL ARTS, VOLLEYBALL, FRISBEE, NETBALL",
            imageRes = R.drawable.ara4,
            address = "Evolve Concept Mall, Jalan PJU 1a/4, Ara Damansara, 47301 Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Food Court", "Drinks", "Toilets", "Equipment Rental"),
            courts = 10,
            price = "RM40/hr"
        ),
        Venue(
            id = "ara5",
            name = "Sri KDU International School, Kota Damansara",
            location = "Petaling Jaya, Selangor",
            sportType = "FUTSAL, DODGEBALL, BASKETBALL, HANDBALL, BADMINTON, VOLLEYBALL, FOOTBALL, FRISBEE, NETBALL, SWIMMING, TENNIS",
            imageRes = R.drawable.ara5,
            address = "Sri KDU International School, Kota Damansara, Petaling Jaya",
            amenities = listOf("Parking", "Cafeteria", "Changing Rooms", "Showers", "Equipment Rental"),
            courts = 12,
            price = "RM50/hr"
        ),
        Venue(
            id = "ara6",
            name = "The New Camp",
            location = "Petaling Jaya, Selangor",
            sportType = "FOOTBALL, FRISBEE, FLAG FOOTBALL",
            imageRes = R.drawable.ara6,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 2,
            price = "RM45/hr"
        ),
        Venue(
            id = "ara7",
            name = "Ara Courts Badminton Hall",
            location = "Petaling Jaya, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.ara7,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 8,
            price = "RM35/hr"
        ),
        Venue(
            id = "ara8",
            name = "Pioneer Badminton Hall @ Kota Damansara",
            location = "Petaling Jaya, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.ara8,
            address = "Kota Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 10,
            price = "RM30/hr"
        ),
        Venue(
            id = "ara9",
            name = "Mypickle Lifetime",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara9,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM38/hr"
        ),
        Venue(
            id = "ara10",
            name = "Endfield Football Field, The Club",
            location = "Petaling Jaya, Selangor",
            sportType = "FOOTBALL",
            imageRes = R.drawable.ara10,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Showers", "Equipment Rental"),
            courts = 1,
            price = "RM120/hr"
        ),
        Venue(
            id = "ara11",
            name = "Champion Badminton Court",
            location = "Petaling Jaya, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.ara11,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM32/hr"
        ),
        Venue(
            id = "ara12",
            name = "Pickle Date Club",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara12,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental", "Cafe"),
            courts = 5,
            price = "RM36/hr"
        ),
        Venue(
            id = "ara13",
            name = "Let's Pickleball @ The Starling Mall",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara13,
            address = "The Starling Mall, Ara Damansara, Petaling Jaya",
            amenities = listOf("Parking", "Food Court", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM42/hr"
        ),
        Venue(
            id = "ara14",
            name = "Hevea Pickleball Arena",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara14,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM35/hr"
        ),
        Venue(
            id = "ara15",
            name = "JC Pickleball Centre @ PJ SS2",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara15,
            address = "SS2, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Toilets", "Drinks"),
            courts = 4,
            price = "RM32/hr"
        ),
        Venue(
            id = "ara16",
            name = "Time to Tennis @ 3 Damansara",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL, TENNIS",
            imageRes = R.drawable.ara16,
            address = "3 Damansara, Ara Damansara, Petaling Jaya",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM45/hr"
        ),
        Venue(
            id = "ara17",
            name = "Ajak Pickleground",
            location = "Petaling Jaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.ara17,
            address = "Ara Damansara, Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 3,
            price = "RM34/hr"
        )
    ),
    "Balakong, Selangor" to listOf(
        Venue(
            id = "bl1",
            name = "Cheras Badminton Centre",
            location = "Balakong, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.bl1,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 8,
            price = "RM30/hr"
        ),
        Venue(
            id = "bl2",
            name = "Pickle Valley 2 @ Balakong",
            location = "Seri Kembangan, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl2,
            address = "Seri Kembangan, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 5,
            price = "RM35/hr"
        ),
        Venue(
            id = "bl3",
            name = "Footmax Cheras Sport Centre",
            location = "Balakong, Selangor",
            sportType = "FUTSAL, BASKETBALL",
            imageRes = R.drawable.bl3,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 3,
            price = "RM40/hr"
        ),
        Venue(
            id = "bl4",
            name = "YTS Sport Centre",
            location = "Cheras, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.bl4,
            address = "Cheras, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM32/hr"
        ),
        Venue(
            id = "bl5",
            name = "Apro Pickle Club - Balakong",
            location = "Cheras, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl5,
            address = "Cheras, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM36/hr"
        ),
        Venue(
            id = "bl6",
            name = "Re Serve Sport",
            location = "Kajang, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl6,
            address = "Kajang, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM34/hr"
        ),
        Venue(
            id = "bl7",
            name = "Top Pickle Sport Centre",
            location = "Seri Kembangan, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl7,
            address = "Seri Kembangan, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 5,
            price = "RM38/hr"
        ),
        Venue(
            id = "bl8",
            name = "The Grumpy Pickleball Club",
            location = "Seri Kembangan, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl8,
            address = "Seri Kembangan, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental", "Cafe"),
            courts = 4,
            price = "RM37/hr"
        ),
        Venue(
            id = "bl9",
            name = "Picklestop",
            location = "Cheras, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl9,
            address = "Cheras, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 3,
            price = "RM33/hr"
        ),
        Venue(
            id = "bl10",
            name = "Sportizza Badminton - Home of Sports Balakong",
            location = "Balakong, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.bl10,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 8,
            price = "RM31/hr"
        ),
        Venue(
            id = "bl11",
            name = "Double Bounce Balakong",
            location = "Balakong, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl11,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM35/hr"
        ),
        Venue(
            id = "bl12",
            name = "PICKLD",
            location = "Balakong, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl12,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 5,
            price = "RM36/hr"
        ),
        Venue(
            id = "bl13",
            name = "Hyprground Pickleball - Balakong",
            location = "Balakong, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bl13,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM37/hr"
        ),
        Venue(
            id = "bl14",
            name = "149 Sports Arena",
            location = "Balakong, Selangor",
            sportType = "BADMINTON, DODGEBALL, VOLLEYBALL, FRISBEE, FITNESS SPACE, PICKLEBALL, LIGHT VOLLEYBALL",
            imageRes = R.drawable.bl14,
            address = "Balakong, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental", "Gym"),
            courts = 8,
            price = "RM40/hr"
        )
    ),
    "Bandar Baru Bangi, Selangor" to listOf(
        Venue(
            id = "bbb1",
            name = "LaVista Arena, Bandar Baru Bangi",
            location = "Bandar Baru Bangi, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bbb1,
            address = "Bandar Baru Bangi, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM35/hr"
        ),
        Venue(
            id = "bbb2",
            name = "The PicklePlay Club @ Bangi",
            location = "Bandar Baru Bangi, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bbb2,
            address = "Bandar Baru Bangi, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 5,
            price = "RM36/hr"
        ),
        Venue(
            id = "bbb3",
            name = "Club 360 @ Setia Ecohill",
            location = "Semenyih, Selangor",
            sportType = "FUTSAL, BASKETBALL, PICKLEBALL, BADMINTON",
            imageRes = R.drawable.bbb3,
            address = "Semenyih, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM38/hr"
        ),
        Venue(
            id = "bbb4",
            name = "Double Bounce Semenyih",
            location = "Semenyih, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bbb4,
            address = "Semenyih, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM35/hr"
        ),
        Venue(
            id = "bbb5",
            name = "KB Semenyih Badminton Centre",
            location = "Semenyih, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.bbb5,
            address = "Semenyih, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 8,
            price = "RM30/hr"
        ),
        Venue(
            id = "bbb6",
            name = "Athlon Sports Centre",
            location = "Semenyih, Selangor",
            sportType = "PICKLEBALL, TABLE TENNIS, FUTSAL",
            imageRes = R.drawable.bbb6,
            address = "Semenyih, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 5,
            price = "RM37/hr"
        ),
        Venue(
            id = "bbb7",
            name = "EZY Badminton Centre",
            location = "Semenyih, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.bbb7,
            address = "Semenyih, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM32/hr"
        )
    ),
    "Bandar Utama, Selangor" to listOf(
        Venue(
            id = "bu1",
            name = "The New Camp",
            location = "Petaling Jaya, Selangor",
            sportType = "FLAG FOOTBALL, FRISBEE, FOOTBALL",
            imageRes = R.drawable.ara6,
            address = "Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 2,
            price = "RM45/hr"
        ),
        Venue(
            id = "bu2",
            name = "Champion Badminton Court",
            location = "Petaling Jaya, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.ara11,
            address = "Petaling Jaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM32/hr"
        )
    ),
    "Batang Kali, Selangor" to listOf(
        Venue(
            id = "btk1",
            name = "Pickle Arena",
            location = "Batang Kali, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.bk1,
            address = "Batang Kali, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM28/hr"
        ),
        Venue(
            id = "btk2",
            name = "BB Smash Badminton & Pickleball Centre",
            location = "Rawang, Selangor",
            sportType = "PICKLEBALL, BADMINTON",
            imageRes = R.drawable.bk2,
            address = "Batang Kali, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets"),
            courts = 2,
            price = "RM25/hr"
        )
    ),
    "Batu Caves, Selangor" to listOf(
        Venue(
            id = "bc1",
            name = "Ultra Sports Center",
            location = "Batu Caves, Selangor",
            sportType = "PICKLEBALL, TABLE TENNIS, BADMINTON",
            imageRes = R.drawable.bc1,
            address = "Batu Caves, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 6,
            price = "RM30/hr"
        )
    ),
    "Bukit Beruntung, Selangor" to listOf(
        Venue(
            id = "bb1",
            name = "BB Smash Badminton & Pickleball Centre",
            location = "Rawang, Selangor",
            sportType = "PICKLEBALL, BADMINTON",
            imageRes = R.drawable.bk2,
            address = "Bukit Beruntung, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 5,
            price = "RM25/hr"
        ),
        Venue(
            id = "bb2",
            name = "Dewan Komuniti Serendah",
            location = "Serendah, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.bb2,
            address = "Bukit Beruntung, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets"),
            courts = 3,
            price = "RM28/hr"
        )
    ),
    "Cyberjaya, Selangor" to listOf(
        Venue(
            id = "cy1",
            name = "Shaftsbury Sports",
            location = "Cyberjaya, Selangor",
            sportType = "SEPAK TAKRAW, BADMINTON, BASKETBALL",
            imageRes = R.drawable.cy1,
            address = "Cyberjaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Showers", "Equipment Rental"),
            courts = 8,
            price = "RM38/hr"
        ),
        Venue(
            id = "cy2",
            name = "Hyprground Pickleball - Cyberjaya",
            location = "Cyberjaya, Selangor",
            sportType = "PICKLEBALL",
            imageRes = R.drawable.cy2,
            address = "Cyberjaya, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Equipment Rental"),
            courts = 4,
            price = "RM35/hr"
        )
    ),
    "Kajang, Selangor" to listOf(
        Venue(
            id = "kj1",
            name = "Sungai Chua Kajang Badminton Centre",
            location = "Kajang, Selangor",
            sportType = "BADMINTON",
            imageRes = R.drawable.kj1,
            address = "Kajang, Selangor",
            amenities = listOf("Parking", "Drinks", "Toilets", "Showers", "Equipment Rental"),
            courts = 10,
            price = "RM35/hr"
        )
    )
)