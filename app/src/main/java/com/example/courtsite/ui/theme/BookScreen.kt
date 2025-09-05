package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.courtsite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(navController: NavController? = null) {
    val navBackStackEntry by navController?.currentBackStackEntryAsState() ?: remember { mutableStateOf(null) }
    var selectedSport by remember { mutableStateOf("Select a sport") }
    var searchLocation by remember { mutableStateOf("") }
    var showSportSheet by remember { mutableStateOf(false) }
    var showLocationSheet by remember { mutableStateOf(false) }

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
                        IconButton(onClick = { navController?.popBackStack() }) {
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
                // Rounded rectangle container
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
                                Text(
                                    text = selectedSport,
                                    color = if (selectedSport == "Select a sport") Color.Gray else Color.Black,
                                    fontSize = 14.sp
                                )
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
                                    text = if (searchLocation.isEmpty()) "Search venue name, city, or state" else searchLocation,
                                    color = if (searchLocation.isEmpty()) Color.Gray else Color(0xFF4E28CC),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Search Button
                        Button(
                            onClick = { /* TODO: Handle search */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4E28CC),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
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
                SportSelectionSheet(
                    onSportSelected = { sport ->
                        selectedSport = sport
                        showSportSheet = false
                    }
                )
            }
        }

        // Location Bottom Sheet
        if (showLocationSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLocationSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                LocationSearchSheet(
                    searchText = searchLocation,
                    onSearchTextChanged = { searchLocation = it },
                    onDismiss = { showLocationSheet = false }
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

@Composable
fun SportSelectionSheet(onSportSelected: (String) -> Unit) {
    val sportCategories = listOf(
        "Badminton", "Pickleball", "Futsal", "Basketball", "Tennis",
        "Table Tennis", "Volleyball", "Netball", "Swimming", "Bowling", "Gym", "Wall Climbing"
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

        // Sport type filter buttons
        val sportTypes = listOf("All", "Racquet", "Team", "Water", "Fitness", "Recreation")
        var selectedType by remember { mutableStateOf("All") }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sportTypes) { type ->
                BookSportTypeFilterButton(
                    sport = type,
                    isSelected = type == selectedType,
                    onClick = { selectedType = type }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sports list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sportCategories) { sport ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSportSelected(sport) }
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = sport,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun BookSportTypeFilterButton(sport: String, isSelected: Boolean, onClick: () -> Unit) {
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
fun LocationSearchSheet(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
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

        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            placeholder = { Text("Search venue name, city, or state") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Use current location",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4E28CC)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Venues",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Venues list
        listOf(
            "Dewan Badminton MSN",
            "Pickle Lane Society",
            "JC Pickleball Centre @ PJ SS2",
            "Tops Arena Empire",
            "Pickle Royal"
        ).forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.building),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Location list
        listOf(
            "Ampang, Selangor",
            "Ara Damansara, Selangor",
            "Balakong, Selangor",
            "Bandar Baru Bangi, Selangor"
        ).forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BookScreenPreview() {
    BookScreen()
}