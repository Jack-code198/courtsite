package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.courtsite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Tournament details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            actions = {
                IconButton(onClick = {
                    navController.navigate("tournaments") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Badminton tag and venue info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Badminton tag
                    Card(
                        modifier = Modifier.padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF7C4DFF)
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "BADMINTON",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Venue name
                    Text(
                        text = "Lavana Sports Centre @ Setapak",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Location
                    Text(
                        text = "Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Venue image
                Image(
                    painter = painterResource(id = R.drawable.lavana),
                    contentDescription = "Lavana Sports Centre",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Date and time
            Text(
                text = "30 Sep 2025, Tuesday",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "01:00PM - 03:00PM",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Court info
            Text(
                text = "Court 6",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = "Single",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Divider
            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Price summary
            Text(
                text = "PRICE SUMMARY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Subtotal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Subtotal",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "RM 7.00",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "RM 7.00",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Join Now button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C4DFF)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Join Now",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}