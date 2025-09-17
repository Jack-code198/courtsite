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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.navigation.compose.rememberNavController
import com.example.courtsite.R
import com.example.courtsite.data.model.venuesByLocation
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(navController: NavController? = null, preselectedSport: String? = null) {
    val localNavController = rememberNavController()
    val currentNavController = navController ?: localNavController
    var selectedSport by remember { mutableStateOf(preselectedSport ?: "") }
    var selectedLocation by remember { mutableStateOf("") }
    var showSportSheet by remember { mutableStateOf(false) }
    var showLocationSheet by remember { mutableStateOf(false) }

    // If navigated with a sport from Explore, open the sport sheet to show synced selection
    LaunchedEffect(preselectedSport) {
        if (!preselectedSport.isNullOrBlank()) {
            selectedSport = preselectedSport
            showSportSheet = true
        }
    }

    // Add this function to handle navigation
    fun navigateToResults(location: String, sport: String) {
        try {
            val encodedLocation = URLEncoder.encode(location, "UTF-8")
            val encodedSport = URLEncoder.encode(sport, "UTF-8")
            currentNavController.navigate("bookingResults/$encodedLocation/$encodedSport") {
                launchSingleTop = true
            }
        } catch (e: Exception) {
            println("Navigation error: ${e.message}")
            e.printStackTrace()
        }
    }

    // Calculate if search button should be enabled - EITHER sport OR location
    val isSearchEnabled = selectedLocation.isNotEmpty() || selectedSport.isNotEmpty()

    Box(modifier = Modifier.fillMaxSize()) {
        // Wave background at bottom
        Image(
            painter = painterResource(id = R.drawable.wave),
            contentDescription = "Wave background",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Book to Play",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { currentNavController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Search form
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Sport Selection
                        Text("Sport", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(1.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable { showSportSheet = true }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.sport),
                                    contentDescription = "Sport",
                                    modifier = Modifier.size(35.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (selectedSport.isBlank()) {
                                    Text(
                                        text = "Select a sport",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                } else {
                                    Text(
                                        text = selectedSport,
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Location Search
                        Text("Where", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(1.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable { showLocationSheet = true }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.location),
                                    contentDescription = "Location",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedLocation.isEmpty()) "Search venue name, city, or state" else selectedLocation,
                                    color = if (selectedLocation.isEmpty()) Color.Gray else Color(0xFF4E28CC),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Search Button - Enable if EITHER sport OR location is selected
                        Button(
                            onClick = {
                                // Allow search with either location OR sport selected
                                if (selectedLocation.isNotEmpty() || selectedSport.isNotEmpty()) {
                                    navigateToResults(selectedLocation, selectedSport)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4E28CC), // Always blue
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF4E28CC).copy(alpha = 0.5f), // Semi-transparent when disabled
                                disabledContentColor = Color.White.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = isSearchEnabled // Enable if either field has selection
                        ) {
                            Text("Search", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Sport Bottom Sheet
        if (showSportSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSportSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                BookSportSelectionSheet(
                    selectedSport = selectedSport,
                    onSportSelected = { sport ->
                        selectedSport = sport
                        showSportSheet = false
                        // Auto-search if location is already selected
                        if (selectedLocation.isNotEmpty()) {
                            navigateToResults(selectedLocation, sport)
                        }
                    }
                )
            }
        }

        // Location Bottom Sheet - MODIFIED TO NAVIGATE DIRECTLY WHEN VENUE IS SELECTED
        if (showLocationSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLocationSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                LocationSearchSheetBasic(
                    searchText = selectedLocation,
                    onSearchTextChanged = {
                        selectedLocation = it
                    },
                    onDismiss = { showLocationSheet = false },
                    onLocationSelected = { selectedItem ->
                        selectedLocation = selectedItem
                        showLocationSheet = false

                        // NAVIGATE DIRECTLY TO RESULTS WHEN A VENUE IS SELECTED
                        navigateToResults(selectedItem, selectedSport)
                    }
                )
            }
        }
    }
}

@Composable
fun BookBottomNavItem(
    iconRes: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .width(64.dp)
    ) {
        // Background for selected item
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isSelected) Color(0xFF4E28CC).copy(alpha = 0.1f) else Color.Transparent
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = if (isSelected) Color(0xFF4E28CC) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            fontSize = 10.sp,
            color = if (isSelected) Color(0xFF4E28CC) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

data class SportCategory(
    val name: String,
    val sports: List<String>,
    val type: String
)

@Composable
fun BookSportSelectionSheet(selectedSport: String, onSportSelected: (String) -> Unit) {
    // Add "All Sports" as the first option
    data class SportItem(val name: String, val iconResId: Int, val type: String)
    val sports: List<SportItem> = listOf(
        SportItem("All Sports", R.drawable.all_sports, "All"), // Add this line
        // Racquet
        SportItem("Pickleball", R.drawable.pickleball, "Racquet"),
        SportItem("Badminton", R.drawable.badminton, "Racquet"),
        SportItem("Padel", R.drawable.padel, "Racquet"),
        SportItem("Squash", R.drawable.squash, "Racquet"),
        SportItem("Tennis", R.drawable.tennis, "Racquet"),
        SportItem("Table Tennis", R.drawable.table_tennis, "Racquet"),
        // Team
        SportItem("Futsal", R.drawable.futsal, "Team"),
        SportItem("Football", R.drawable.football, "Team"),
        SportItem("Volleyball", R.drawable.volleyball, "Team"),
        SportItem("3x3 Basketball", R.drawable.basketball_3x3, "Team"),
        SportItem("Field Hockey", R.drawable.field_hockey, "Team"),
        SportItem("Basketball", R.drawable.basketball, "Team"),
        SportItem("Dodgeball", R.drawable.dodgeball, "Team"),
        SportItem("Lawn Bowl", R.drawable.lawn_bowl, "Team"),
        SportItem("Frisbee", R.drawable.frisbee, "Team"),
        SportItem("Indoor Hockey", R.drawable.indoor_hockey, "Team"),
        SportItem("Captain Ball", R.drawable.captain_ball, "Team"),
        SportItem("Sepak Takraw", R.drawable.sepak_takraw, "Team"),
        SportItem("Handball", R.drawable.handball, "Team"),
        SportItem("Teqball", R.drawable.teqball, "Team"),
        SportItem("Flag Football", R.drawable.flag_football, "Team"),
        SportItem("Rugby", R.drawable.rugby, "Team"),
        // Water
        SportItem("Free Diving", R.drawable.free_diving, "Water"),
        SportItem("Mermaiding", R.drawable.mermaiding, "Water"),
        SportItem("Scuba Diving", R.drawable.scuba_diving, "Water"),
        SportItem("Swimming", R.drawable.swimming, "Water"),
        // Recreation
        SportItem("Bowling", R.drawable.bowling, "Recreation"),
        SportItem("Bumper Car", R.drawable.bumper_car, "Recreation"),
        SportItem("Foosball", R.drawable.foosball, "Recreation"),
        SportItem("Golf Driving Range", R.drawable.golf_driving_range, "Recreation"),
        SportItem("Go-Kart", R.drawable.go_kart, "Recreation"),
        SportItem("Martial Arts", R.drawable.martial_arts, "Recreation"),
        SportItem("Pool Table", R.drawable.pool_table, "Recreation"),
        SportItem("Rollerblading", R.drawable.rollerblading, "Recreation"),
        // Fitness
        SportItem("Dance Studio", R.drawable.dance_studio, "Fitness"),
        SportItem("Fitness Space", R.drawable.fitness_space, "Fitness"),
        SportItem("Gym", R.drawable.gym, "Fitness"),
        SportItem("Gymnastic", R.drawable.gymnastic, "Fitness"),
        SportItem("Running Track", R.drawable.running_track, "Fitness"),
        SportItem("Wall Climbing", R.drawable.wall_climbing, "Fitness"),
        // Event Spaces
        SportItem("Event Space", R.drawable.event_space, "Event Spaces"),
        SportItem("Sporty", R.drawable.sporty_celebration, "Event Spaces"),
        SportItem("Event Room", R.drawable.event_room, "Event Spaces"),
        // Stay
        SportItem("Chalet", R.drawable.chalet, "Stay"),
        // Classes / Combat
        SportItem("Boxing", R.drawable.boxing, "Classes"),
        SportItem("Brazilian Ju-Jitsu", R.drawable.brazilian_jiu_jitsu, "Classes"),
        SportItem("Capoeira", R.drawable.capoeira, "Classes"),
        SportItem("Fitness", R.drawable.fitness, "Classes"),
        SportItem("Fighter's Strength And Conditioning", R.drawable.fighters_strength_and_conditioning, "Classes"),
        SportItem("Grappling", R.drawable.grappling, "Classes"),
        SportItem("Kickboxing", R.drawable.kickboxing, "Classes"),
        SportItem("MMA", R.drawable.mma, "Classes"),
        SportItem("Muay Thai", R.drawable.muay_thai, "Classes"),
        SportItem("Muay Thai Fitness", R.drawable.muay_thai_fitness, "Classes"),
        SportItem("Taekwondo", R.drawable.taekwondo, "Classes"),
        // Other
        SportItem("Light Volleyball", R.drawable.light_volleyball, "Other")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Select Sport",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sport type filter buttons (include "All")
        val sportTypes = listOf("All", "Racquet", "Team", "Water", "Recreation", "Fitness", "Event Spaces", "Stay", "Classes", "Other")
        var selectedType by remember { mutableStateOf("All") }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sportTypes) { type ->
                SportTypeChip(
                    sport = type,
                    isSelected = type == selectedType,
                    onClick = { selectedType = type }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sports grid with images
        val filteredSports = remember(selectedType, sports) {
            if (selectedType == "All") sports else sports.filter { it.type == selectedType }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSports) { item ->
                val isSelected = selectedSport.equals(item.name, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF4E28CC).copy(alpha = if (isSelected) 0.7f else 0.2f), RoundedCornerShape(12.dp))
                        .clickable { onSportSelected(item.name) },
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.name,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.7f),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Remove text label under sport image as requested
                    }
                }
            }
        }
    }
}

@Composable
fun SportTypeChip(sport: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF4E28CC) else Color.White
    val textColor = if (isSelected) Color.White else Color(0xFF4E28CC)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(
                1.dp,
                Color(0xFF4E28CC),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = sport,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BookingCard(
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
            .height(350.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(sportType, fontSize = 12.sp, color = Color.Gray)
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(location, fontSize = 12.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
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

@Composable
fun LocationSearchSheetBasic(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit  // This will now trigger navigation
) {
    // Group locations by first letter for alphabetical organization
    val locations = listOf(
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

    // Get all venue names from the venues data
    val allVenueNames = remember {
        venuesByLocation.flatMap { it.value }.map { it.name }.distinct()
    }

    // Filter venues based on search text
    val filteredVenues = remember(searchText, allVenueNames) {
        if (searchText.isBlank()) {
            allVenueNames
        } else {
            allVenueNames.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    // Filter locations based on search text
    val filteredLocations = remember(searchText, locations) {
        if (searchText.isBlank()) {
            locations.groupBy { it.first().uppercaseChar() }.toSortedMap()
        } else {
            val filtered = locations
                .filter { it.contains(searchText, ignoreCase = true) }
                .groupBy { it.first().uppercaseChar() }
            filtered.toSortedMap()
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
                                    // When venue is clicked, call onLocationSelected which will navigate
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
                                        // When location is clicked, just update the text field
                                        onSearchTextChanged(location)
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
                            modifier = Modifier.padding(vertical = 16.dp)
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

@Preview(showBackground = true)
@Composable
fun BookScreenPreview() {
    BookScreen()
}