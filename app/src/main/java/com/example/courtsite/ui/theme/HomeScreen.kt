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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.courtsite.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.core.content.res.ResourcesCompat
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
<<<<<<< HEAD
=======
import com.example.tournaments.TournamentsScreen
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02


val COURTSITE_LOGO = R.drawable.courtsitewhite
val BACKGROUND_IMAGE = R.drawable.dato1

val NavBarColor = Color(0xFFFFF9F9)
val HomeTextColor = Color(0xFF6B42F0)
val OtherTextColor = Color(0xBF000000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabs(outerNavController: NavController? = null) {
    val tabsNavController = rememberNavController()
    val navBackStackEntry by tabsNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    Scaffold(
        bottomBar = {
            if (currentRoute != "settings") {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onLogout = { /* Optional: delegate to outerNavController if needed */ },
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            tabsNavController.navigate(route) {
                                // Clear back stack to avoid building up navigation history
                                popUpTo(tabsNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabsNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreenContent(navController = tabsNavController)
            }
<<<<<<< HEAD
=======
            // availability route is per-venue now
            composable("availability/{venueName}/{sportType}") { backStackEntry ->
                val venueName = backStackEntry.arguments?.getString("venueName") ?: ""
                val sportType = backStackEntry.arguments?.getString("sportType") ?: ""
                AvailabilityScreen(navController = tabsNavController, venueName = venueName, sportType = sportType)
            }
            composable("booknow/{venueName}/{sportType}") { backStackEntry ->
                val venueName = backStackEntry.arguments?.getString("venueName") ?: ""
                val sportType = backStackEntry.arguments?.getString("sportType") ?: ""
                BookNowScreen(navController = tabsNavController, venueName = venueName, sportType = sportType)
            }
            composable("cart") {
                CartScreen(navController = tabsNavController)
            }
            composable("payment-method") {
                PaymentMethodScreen(navController = tabsNavController)
            }
            composable("payment") {
                PaymentScreen(navController = tabsNavController)
            }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            composable("explore") {
                ExploreScreen(navController = tabsNavController) // This should work now
            }
            composable("book") {
                BookScreen(navController = tabsNavController)
            }
            // Allow preselected sport from Explore: book/{sport}
            composable("book/{sport}") { backStackEntry ->
                val sportArg = backStackEntry.arguments?.getString("sport")
                BookScreen(navController = tabsNavController, preselectedSport = sportArg)
            }
            composable("upcoming") {
                UpcomingScreen(navController = tabsNavController)
            }
            composable("profile") {
                ProfileScreen(navController = tabsNavController)
            }
            // Edit profile route within tabs
            composable("editProfile") {
                EditProfileScreen(navController = tabsNavController)
            }
            // Settings route within tabs
            composable("settings") {
                SettingScreen(navController = tabsNavController)
            }
<<<<<<< HEAD
            // Create Password route within tabs
            composable("createPassword") {
                CreatePasswordScreen(navController = tabsNavController)
            }
=======
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            // ADD THE BOOKING RESULTS ROUTE HERE
            composable("bookingResults/{location}/{sport}") { backStackEntry ->
                val location = backStackEntry.arguments?.getString("location") ?: ""
                val sport = backStackEntry.arguments?.getString("sport") ?: ""
                BookingResultsScreen(
                    navController = tabsNavController,
                    location = location,
                    sport = sport
                )
            }
            // Venue Details route
            composable("venueDetails/{venueName}") { backStackEntry ->
                val venueName = backStackEntry.arguments?.getString("venueName") ?: ""
                VenueDetailsScreen(navController = tabsNavController, venueName = venueName)
            }
<<<<<<< HEAD
            // Facility Management route
            composable("facilityManagement") {
                // Placeholder for facility management screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Facility Management - Coming Soon")
                }
            }
            
            // Help Centre route
            composable("helpCentre") {
                // Placeholder for help centre screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Help Centre - Coming Soon")
                }
            }
            
            // Contact Form route
            composable("contactForm") {
                // Placeholder for contact form screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Contact Form - Coming Soon")
                }
            }
            
            // Feedback route
            composable("feedback") {
                FeedbackScreen(navController = tabsNavController)
            }
            
            // Feedback management route
            composable("feedbackManagement") {
                FeedbackManagementScreen(navController = tabsNavController)
            }
            
            // Delegate logout route inside tabs to root nav controller
            composable("logout") {
                outerNavController?.navigate("logout") {
                    popUpTo(0) { inclusive = true }
                }
=======
            composable("tournaments") {
                TournamentsScreen(navController = tabsNavController)
            }
            composable("tournamentDetail"){
                TournamentDetailsScreen(navController = tabsNavController)
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController? = null, onLogout: () -> Unit = {}) {
    // Get current route from navigation controller
    val navBackStackEntry by navController?.currentBackStackEntryAsState() ?: remember { mutableStateOf(null) }
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onLogout = onLogout,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController?.navigate(route) {
                            // Clear back stack to avoid building up navigation history
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        HomeScreenContent(modifier = Modifier.padding(innerPadding), navController = navController)
    }
}

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier, navController: NavController? = null) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { HeaderSection(navController = navController) }
        item { WhyNotBookAgainSection() }
        item { SportsCentreCard() }
        item { FeaturedEventsSection() }
        item { PartnerSpotlightSection() }
        item { TrendingVenuesSection() }
        item { StayActiveSection() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(navController: NavController? = null) {
    var searchText by remember { mutableStateOf("") }
    var showSearchModal by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        // Banner with rounded corners
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(15.dp))
        ) {
            Image(
                painter = painterResource(id = BACKGROUND_IMAGE),
                contentDescription = "Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = COURTSITE_LOGO),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "courtsite",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Search bar - MODIFIED TO NAVIGATE TO BOOK SCREEN
        Box(
            modifier = Modifier
                .offset(y = (-25).dp)
                .width(350.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    // Navigate directly to BookScreen when search bar is clicked
                    navController?.navigate("book") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search Place, Location or Sport",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

<<<<<<< HEAD
        // Four square buttons
=======
        // Four square buttons (remove direct Availability entry)
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            SquareButton(
                iconRes = R.drawable.book,
                label = "Book",
                onClick = {
                    navController?.navigate("book") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            SquareButton(R.drawable.join, "Join")
<<<<<<< HEAD
            SquareButton(R.drawable.tournaments, "Tournament")
=======
            SquareButton(
                R.drawable.tournaments,
                "Tournament",
                onClick = {
                    navController?.navigate("tournaments") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            SquareButton(R.drawable.deals, "Deals")
        }
    }

    // Modal Bottom Sheet
    if (showSearchModal) {
        ModalBottomSheet(
            onDismissRequest = { showSearchModal = false },
            sheetState = sheetState
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

            // Filter locations based on search text (starts with search text)
            val filteredLocations = remember(searchText, locations) {
                if (searchText.isBlank()) {
                    // If no search text, group all locations by first letter
                    locations.groupBy { it.first().uppercaseChar() }
                        .toSortedMap()
                } else {
                    // Filter locations that start with the search text (case insensitive)
                    val filtered = locations
                        .filter { it.startsWith(searchText, ignoreCase = true) }
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
                    Text("Where", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { showSearchModal = false }) {
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

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search venue name, city, or state") },
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
                Text("Use current location", fontWeight = FontWeight.Bold, color = Color(0xFF4E28CC))

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
                        // Venues section
                        item {
                            Text("Venues", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Venues list with building icon
                        items(
                            listOf(
                                "Dewan Badminton MSN",
                                "Pickle Lane Society",
                                "JC Pickleball Centre @ PJ SS2",
                                "Tops Arena Empire",
                                "Pickle Royal"
                            )
                        ) { venue ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        searchText = venue
                                        showSearchModal = false
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

                        // Divider between sections
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Locations section
                        item {
                            Text("Location", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Alphabetically organized location list
                        if (filteredLocations.isEmpty()) {
                            item {
                                Text(
                                    text = "No locations found",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
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
                                                searchText = location
                                                showSearchModal = false
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
    }
}

@Composable
fun SquareButton(iconRes: Int, label: String, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(80.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = true
                ),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WhyNotBookAgainSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = "Why Not Book Again?",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        DividerLine()
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    )
}

@Composable
fun SportsCentreCard() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .width(200.dp)
            .height(225.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.lavana),
                contentDescription = "Lavana Sports Centre",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Lavana Sports Centre @ Setapak",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Kuala Lumpur, Wilayah Persekutuan",
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* TODO: Add action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Book Now", color = Color.White)
            }
        }
    }
}

@Composable
fun FeaturedEventsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Featured Events & Rewards",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(8.dp))
        EventCard(
            title = "MILO Ultimate Pickleball 2025",
            subtitle = "Ad · MILO MALAYSIA"
        )
    }
}

@Composable
fun EventCard(title: String, subtitle: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.milo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(title, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
fun PartnerSpotlightSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Partner Spotlight", fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(8.dp))

        PartnerCard(
            imageRes = R.drawable.tomaz,
            sportType = "Pickleball",
            name = "Tomaz Pickleball Club",
            location = "Subang Jaya, Selangor"
        )
    }
}

@Composable
fun PartnerCard(
    imageRes: Int,
    sportType: String,
    name: String,
    location: String
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
                    onClick = { /* TODO: View details */ },
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
                    onClick = { /* TODO: Book now */ },
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
fun TrendingVenuesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Trending Venues",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sport filter options
        val sportTypes = listOf("Pickleball", "Badminton", "Futsal", "Basketball", "Tennis")
        var selectedSport by remember { mutableStateOf("Pickleball") }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sportTypes) { sport ->
                SportTypeFilterButton(
                    sport = sport,
                    isSelected = sport == selectedSport,
                    onClick = { selectedSport = sport }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(16.dp))

        // All venues
        val allVenues = listOf(
            TrendingVenue(
                sportType = "Pickleball",
                name = "Pickle Lane Society",
                location = "Seri Kembangan, Selangor",
                imageRes = R.drawable.lane1
            ),
            TrendingVenue(
                sportType = "Pickleball",
                name = "JC Pickleball Centre",
                location = "Petaling Jaya, Selangor",
                imageRes = R.drawable.lane2
            ),
            TrendingVenue(
                sportType = "Pickleball",
                name = "Super Pickle Hub",
                location = "Kuala Lumpur",
                imageRes = R.drawable.lane3
            ),
            TrendingVenue(
                sportType = "Badminton",
                name = "Smash Arena",
                location = "Shah Alam, Selangor",
                imageRes = R.drawable.lane4
            )
        )

        val filteredVenues = allVenues.filter { it.sportType.equals(selectedSport, ignoreCase = true) }

        if (filteredVenues.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = 0.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredVenues) { venue ->
                    TrendingVenueCard(venue = venue)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No venues available for $selectedSport",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TrendingVenueCard(
    venue: TrendingVenue,
    startPadding: Dp = 0.dp
) {
    Card(
        modifier = Modifier
            .padding(start = startPadding, end = 8.dp)
            .width(230.dp)
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            // Image
            Image(
                painter = painterResource(id = venue.imageRes),
                contentDescription = venue.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Venue details
            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = venue.sportType,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = venue.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = venue.location,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: View details */ },
                    border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.view),
                        contentDescription = "View",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View", fontSize = 12.sp)
                }

                Button(
                    onClick = { /* TODO: Book now */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Text("Book Now", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SportTypeFilterButton(sport: String, isSelected: Boolean, onClick: () -> Unit) {
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

data class TrendingVenue(
    val sportType: String,
    val name: String,
    val location: String,
    val imageRes: Int
)

@Composable
fun StayActiveSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // --- Stay Active, Stay Safe Card ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Stay Active, Stay Safe",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    val cardWidth = 120.dp
                    val imageSize = 100.dp

                    item {
                        StayActiveCard(
                            painter = painterResource(id = R.drawable.joinfun),
                            text = "Join the fun",
                            cardWidth = cardWidth,
                            imageSize = imageSize
                        )
                    }
                    item {
                        StayActiveCard(
                            painter = painterResource(id = R.drawable.play),
                            text = "Play your part",
                            cardWidth = cardWidth,
                            imageSize = imageSize
                        )
                    }
                    item {
                        StayActiveCard(
                            painter = painterResource(id = R.drawable.help),
                            text = "Call these numbers for help",
                            cardWidth = cardWidth,
                            imageSize = imageSize
                        )
                    }
                    item {
                        StayActiveCard(
                            painter = painterResource(id = R.drawable.minutes),
                            text = "Only takes 5 minutes",
                            cardWidth = cardWidth,
                            imageSize = imageSize
                        )
                    }
                }
            }
        }

        // --- Did You Know? Card with PNGs ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Did You Know? Handy App Tips",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    val cardWidth = 120.dp
                    val imageSize = 100.dp

                    item {
                        StayActiveCard(
                            painter = painterResource(id = R.drawable.tap),
                            text = "One tap to open, just like an app.",
                            cardWidth = cardWidth,
                            imageSize = imageSize
                        )
                    }
                    item {
                        StayActiveCard(
                            painter = painterResource(id = R.drawable.calls),
                            text = "No calls needed, do it in the app",
                            cardWidth = cardWidth,
                            imageSize = imageSize
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StayActiveCard(
    painter: Painter,
    text: String,
    cardWidth: Dp,
    imageSize: Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(cardWidth)
            .clickable { /* Handle click */ }
    ) {
        Image(
            painter = painter,
            contentDescription = text,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String? = null,
    onLogout: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        // Match BookScreen bottom bar styling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    clip = true
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BookBottomNavItem(
                    iconRes = R.drawable.homeon,
                    text = "Home",
                    isSelected = currentRoute == "home",
                    onClick = { onNavigate("home") }
                )
                BookBottomNavItem(
                    iconRes = R.drawable.exploreon,
                    text = "Explore",
                    isSelected = currentRoute == "explore",
                    onClick = { onNavigate("explore") }
                )
                BookBottomNavItem(
                    iconRes = R.drawable.bookon,
                    text = "Book",
                    isSelected = currentRoute == "book",
                    onClick = { onNavigate("book") }
                )
                BookBottomNavItem(
                    iconRes = R.drawable.upcomingon,
                    text = "Upcoming",
                    isSelected = currentRoute == "upcoming",
                    onClick = { onNavigate("upcoming") }
                )
                BookBottomNavItem(
                    iconRes = R.drawable.profileon,
                    text = "Profile",
                    isSelected = currentRoute == "profile",
                    onClick = { onNavigate("profile") }
                )
            }
        }
    }
}

// Preview functions
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        currentRoute = "home",
        onLogout = {},
        onNavigate = {}
    )
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        HomeScreenContent()
    }
}

@Preview(showBackground = true)
@Composable
fun MainTabsPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        MainTabs()
    }
}