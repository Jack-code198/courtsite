package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsite.data.model.VenueDetail
import com.example.courtsite.R

@Composable
fun VenueDetailContent(
    detail: VenueDetail,
    onViewDetails: (() -> Unit)? = null,
    onBookNow: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = detail.imageRes),
            contentDescription = detail.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = detail.sportType,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(detail.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(detail.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            if (!detail.pricePerHour.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("From ${detail.pricePerHour} / hr", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
            if (detail.rating != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Rating ${"%.1f".format(detail.rating)}", fontSize = 12.sp)
            }
            if (!detail.openingHours.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Hours: ${detail.openingHours}", fontSize = 12.sp)
            }
            if (!detail.facilities.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Facilities: ${detail.facilities.joinToString(", ")}", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { onViewDetails?.invoke() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF4E28CC)
                ),
                border = BorderStroke(1.dp, Color(0xFF4E28CC))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.view),
                    contentDescription = null,
                    tint = Color(0xFF4E28CC)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("View")
            }
            Button(
                onClick = { onBookNow?.invoke() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4E28CC),
                    contentColor = Color.White
                )
            ) {
                Text("Book Now")
            }
        }
    }
}

