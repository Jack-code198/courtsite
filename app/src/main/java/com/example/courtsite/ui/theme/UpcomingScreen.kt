package com.example.courtsite.ui.theme

<<<<<<< HEAD
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(navController: NavHostController) {
    val currentRoute = "upcoming"

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Upcoming Screen", style = MaterialTheme.typography.headlineMedium)
=======
import android.R.attr.fontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.courtsite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(navController: NavHostController? = null) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Bookings", "Games")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Upcoming",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = (-24).dp), // Adjust this value as needed
                        textAlign = TextAlign.Center
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController?.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color =  Color(0xFF4E28CC),
                    height = 2.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = if (selectedTab == index) Color(0xFF4E28CC) else Color.Gray
                        )
                    }
                )
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> BookingsContent()
            1 -> GamesContent(navController = navController)
        }

        // REMOVED: Bottom Navigation - this will be handled by MainTabs
        // The MainTabs Scaffold already provides the bottom navigation
    }
}

@Composable
fun BookingsContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // No upcoming bookings message
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main heading text
                Text(
                    text = "You have no upcoming bookings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                // Subtitle text
                Text(
                    text = "Explore your previous sport venues and consider any\nthat you enjoyed!",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            // Venue Card
            VenueCard()
        }
    }
}

@Composable
fun GamesContent(navController: NavHostController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Clock icon/image at the top
        Image(
            painter = painterResource(id = R.drawable.upcomingon), // You'll need to add this icon
            contentDescription = "Clock icon",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Main heading
        Text(
            text = "No upcoming games",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description text
        Text(
            text = "Join our social game and meet new friends. Let's get the fun rolling!",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // "How to join a game?" link
        Text(
            text = "How to join a game?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE),
            modifier = Modifier
                .clickable {
                    navController?.navigate("tournaments") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                .padding(8.dp)
        )
    }
}

@Composable
fun VenueCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            // Venue Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
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
                // Placeholder content
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Venue Image",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    tint = Color.Gray
                )
            }

            // Venue Info
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Lavana Sports Centre @ Setapak",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Text(
                        text = "Kuala Lumpur, Wilayah Persekutuan Kuala\nLumpur",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Book Now Button
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4E28CC)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Book Now",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        }
    }
}