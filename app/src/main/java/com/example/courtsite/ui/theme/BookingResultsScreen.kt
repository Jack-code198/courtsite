package com.example.courtsite.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.courtsite.R
import java.net.URLDecoder
import java.net.URLEncoder
import com.example.courtsite.data.model.Venue
import com.example.courtsite.data.model.venuesByLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingResultsScreen(
    navController: NavController? = null,
    location: String,
    sport: String
) {
    // Decode URL parameters
    val decodedLocation = try {
        URLDecoder.decode(location, "UTF-8")
    } catch (e: Exception) {
        location
    }

    val decodedSport = try {
        URLDecoder.decode(sport, "UTF-8")
    } catch (e: Exception) {
        sport
    }

    // Handle cases where only one parameter is provided
    val effectiveLocation = if (decodedLocation == "Select a sport" || decodedLocation.isEmpty()) "" else decodedLocation
    val effectiveSport = if (decodedSport == "Select a sport" || decodedSport.isEmpty() || decodedSport == "All Sports") "" else decodedSport

    var searchQuery by remember { mutableStateOf("") }
    var showSearchSheet by remember { mutableStateOf(false) }
    var currentDisplayLocation by remember { mutableStateOf(decodedLocation) }

    // Use the shared venues data
    val allVenueNames = remember {
        venuesByLocation.flatMap { it.value }.map { it.name }.distinct()
    }

    // Get all venues for search
    val allVenues = remember {
        venuesByLocation.flatMap { it.value }
    }

    // Group locations by first letter for alphabetical organization
    val allLocations = listOf(
        "Ampang, Selangor",
        "Ara Damansara, Selangor",
        "Balakong, Selangor",
        "Bandar Baru Bangi, Selangor",
        "Bandar Utama, Selangor",
        "Batang Kali, Selangor",
        "Batu Caves, Selangor",
        "Bukit Beruntung, Selangor",
        "Cyberjaya, Selangor",
        "Dengkil, Selangor",
        "Kajang, Selangor",
        "Kapar, Selangor",
        "Klang, Selangor",
        "Kota Damansara, Selangor",
        "North Puchong, Selangor",
        "Pelabuhan Klang, Selangor",
        "Petaling Jaya, Selangor",
        "Puncak Alam, Selangor",
        "Rawang, Selangor",
        "Rimbayu, Selangor",
        "Semenyih, Selangor",
        "Serendah, Selangor",
        "Seri Kembangan, Selangor",
        "Shah Alam, Selangor",
        "South Puchong, Selangor",
        "Subang Jaya, Selangor",
        "Sungai Buloh, Selangor",
        "Sungai Long, Selangor",
        "Telok Panglima Gerang, Selangor",
        "Cheras, W.P Kuala Lumpur",
        "Kampung Baru, W.P Kuala Lumpur",
        "Kepong, W.P Kuala Lumpur",
        "Kuala Lumpur, W.P Kuala Lumpur",
        "Kuchai Lama, W.P Kuala Lumpur",
        "Mont Kiara, W.P Kuala Lumpur",
        "Pandan Indah, W.P Kuala Lumpur",
        "Pudu, W.P Kuala Lumpur",
        "Segambut, W.P Kuala Lumpur",
        "Sentul, W.P Kuala Lumpur",
        "Seputeh, W.P Kuala Lumpur",
        "Setapak, W.P Kuala Lumpur",
        "Sri Petaling, W.P Kuala Lumpur",
        "Sungai Besi, W.P Kuala Lumpur",
        "Batu Pahat, Johor",
        "Gelang Patah, Johor",
        "Iskandar Puteri, Johor",
        "Johor Bahru, Johor",
        "Kota Tinggi, Johor",
        "Nusajaya, Selangor",
        "Skudai, Johor",
        "Tebrau, Johor",
        "Bayan Lepas, Penang",
        "Bukit Mertajam, Penang"
    )

    // Filter venues based on search
    val filteredVenues = remember(searchQuery, currentDisplayLocation, decodedSport, decodedLocation) {
        if (searchQuery.isEmpty()) {
            // Handle partial searches (either location or sport or both)
            val isVenueName = allVenueNames.any { it.equals(effectiveLocation, ignoreCase = true) }

            if (isVenueName && effectiveLocation.isNotEmpty()) {
                // Search by venue name
                allVenues.filter { venue ->
                    venue.name.equals(effectiveLocation, ignoreCase = true) &&
                            (effectiveSport.isEmpty() || effectiveSport == "All Sports" ||
                                    venue.sportType.contains(effectiveSport, ignoreCase = true))
                }
            } else if (effectiveLocation.isNotEmpty()) {
                // Search by location
                val venuesForLocation = venuesByLocation[effectiveLocation] ?: emptyList()
                if (effectiveSport.isEmpty() || effectiveSport == "All Sports") {
                    venuesForLocation
                } else {
                    venuesForLocation.filter { it.sportType.contains(effectiveSport, ignoreCase = true) }
                }
            } else if (effectiveSport.isNotEmpty() && effectiveSport != "Select a sport") {
                // Search by sport only
                allVenues.filter { it.sportType.contains(effectiveSport, ignoreCase = true) }
            } else {
                // No filters, show all venues
                allVenues
            }
        } else {
            // Search query exists, filter across all venues
            allVenues.filter { venue ->
                venue.name.contains(searchQuery, ignoreCase = true) ||
                        venue.location.contains(searchQuery, ignoreCase = true) ||
                        venue.sportType.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // Update the header to show what we're searching for
    val headerText = remember(decodedLocation, searchQuery, decodedSport) {
        if (searchQuery.isEmpty()) {
            if (allVenueNames.any { it.equals(decodedLocation, ignoreCase = true) }) {
                "Venue: $decodedLocation"
            } else if (decodedLocation.isNotEmpty()) {
                "Location: $decodedLocation"
            } else if (decodedSport.isNotEmpty() && decodedSport != "Select a sport") {
                "Sport: $decodedSport"
            } else {
                "All Venues"
            }
        } else {
            "Search Results"
        }
    }

    // Update the sport filter chips
    val sportTypes = listOf(
        "All Sports",
        "Pickleball",
        "Badminton",
        "Tennis",
        "Futsal",
        "Basketball",
        "Volleyball",
        "Football",
        "Multi-Sport"
    )
    var selectedFilter by remember {
        mutableStateOf(
            if (effectiveSport.isNotEmpty()) effectiveSport
            else "All Sports"
        )
    }

    // Apply sport filter to results
    val finalVenues = remember(filteredVenues, selectedFilter) {
        if (selectedFilter == "All Sports") filteredVenues
        else filteredVenues.filter { it.sportType.contains(selectedFilter, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Book to Play",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar with location icon trigger - USING BasicTextField
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(44.dp)
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Location icon
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Location",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showSearchSheet = true }
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // BasicTextField for better control
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search venue name, location, or sport",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    // Search icon
                    IconButton(
                        onClick = {
                            if (searchQuery.isNotEmpty()) {
                                // Perform search
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF4E28CC),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Header section
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = headerText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = if (searchQuery.isEmpty()) {
                        if (decodedSport == "All Sports" || decodedSport.isEmpty()) "" else decodedSport
                    } else {
                        "For: \"$searchQuery\""
                    },
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Sport filter chips under search bar - sync with selection sheet
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sportTypes) { type ->
                    val isSelected = selectedFilter.equals(type, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFF4E28CC) else Color.White)
                            .border(1.dp, Color(0xFF4E28CC), RoundedCornerShape(20.dp))
                            .clickable {
                                selectedFilter = type
                                val encodedLocation = URLEncoder.encode(currentDisplayLocation.ifEmpty { "" }, "UTF-8")
                                val encodedSport = URLEncoder.encode(type, "UTF-8")
                                navController?.navigate("bookingResults/$encodedLocation/$encodedSport") {
                                    popUpTo("bookingResults/{location}/{sport}") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = type,
                            color = if (isSelected) Color.White else Color(0xFF4E28CC),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Results count
            Text(
                text = "${finalVenues.size} venues found",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Venues list or empty state
            if (finalVenues.isEmpty()) {
                // No results found state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_results),
                        contentDescription = "No results found",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No venues found",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please try a different search term or location.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(finalVenues) { venue ->
                        val detail = com.example.courtsite.data.model.VenueDetail(
                            name = venue.name,
                            location = venue.location,
                            sportType = venue.sportType,
                            imageRes = venue.imageRes
                        )
                        VenueDetailContent(
                            detail = detail,
                            onViewDetails = {
                                val encodedName = URLEncoder.encode(venue.name, "UTF-8")
                                navController?.navigate("venueDetails/$encodedName")
                            },
                            onBookNow = {
                                val encodedName = URLEncoder.encode(venue.name, "UTF-8")
                                navController?.navigate("venueDetails/$encodedName")
                            }
                        )
                    }
                }
            }
        }
    }

    // Search Bottom Sheet
    if (showSearchSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSearchSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            BookingLocationSearchSheet(
                searchText = searchQuery,
                onSearchTextChanged = {
                    searchQuery = it
                },
                onDismiss = { showSearchSheet = false },
                onLocationSelected = { selectedLocation ->
                    currentDisplayLocation = selectedLocation
                    searchQuery = ""
                    showSearchSheet = false

                    val encodedLocation = URLEncoder.encode(selectedLocation, "UTF-8")
                    val encodedSport = URLEncoder.encode(selectedFilter, "UTF-8")
                    navController?.navigate("bookingResults/$encodedLocation/$encodedSport") {
                        popUpTo("bookingResults/{location}/{sport}") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                allLocations = allLocations
            )
        }
    }
}

@Composable
fun BookingLocationSearchSheet(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit,
    allLocations: List<String>
) {
    // Generate venue names from shared venues data
    val allVenueNames = remember {
        venuesByLocation.flatMap { it.value }.map { it.name }.distinct()
    }

    // Filter locations based on search text
    val filteredLocations = remember(searchText, allLocations) {
        if (searchText.isBlank()) {
            allLocations.groupBy { it.first().uppercaseChar() }.toSortedMap()
        } else {
            val filtered = allLocations
                .filter { it.contains(searchText, ignoreCase = true) }
                .groupBy { it.first().uppercaseChar() }
            filtered.toSortedMap()
        }
    }

    // Filter venues based on search text
    val filteredVenues = remember(searchText, allVenueNames) {
        if (searchText.isBlank()) {
            allVenueNames
        } else {
            allVenueNames.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Where",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // Search text field
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            placeholder = {
                Text(
                    "Search venue name, city, or state",
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Use current location",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4E28CC),
            modifier = Modifier.clickable { /* Handle current location */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Combined scrollable area for both Venues and Locations
        Box(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
        ) {
            val scrollState = rememberLazyListState()

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                // Venues section - only show if there are venues
                if (filteredVenues.isNotEmpty()) {
                    item {
                        Text("Venues", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(filteredVenues) { venue ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onLocationSelected(venue)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.building),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = venue)
                        }
                    }

                    // Divider between sections if both venues and locations exist
                    if (filteredLocations.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // Locations section
                if (filteredLocations.isNotEmpty()) {
                    item {
                        Text("Location", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Alphabetically organized location list
                    filteredLocations.forEach { (initial, locationsList) ->
                        // Only show section header if we have multiple letters in search results
                        if (filteredLocations.size > 1) {
                            item(key = "header_$initial") {
                                Text(
                                    text = initial.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        // Locations for this letter
                        items(locationsList) { location ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        onLocationSelected(location)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.location),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = location)
                            }
                        }
                    }
                }

                // No results message
                if (filteredVenues.isEmpty() && filteredLocations.isEmpty()) {
                    item {
                        Text(
                            text = "No results found",
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            // Fade effect at the bottom to indicate more content
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.White),
                            startY = 0f,
                            endY = 100f
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EnhancedVenueCard(
    imageRes: Int,
    sportType: String,
    name: String,
    location: String,
    onViewDetails: () -> Unit = {},
    onBookNow: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), // Let the card height adjust to content
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image section - fixed height
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            // Content section - dynamic height
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 4.dp)
            ) {
                Text(
                    text = sportType,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = location,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons section - fixed height
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp), // Add bottom padding
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.view),
                            contentDescription = "View",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View")
                    }
                }

                Button(
                    onClick = onBookNow,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text("Book Now", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingResultsScreenPreview() {
    BookingResultsScreen(
        location = "Ara Damansara, Selangor",
        sport = "PICKLEBALL"
    )
}