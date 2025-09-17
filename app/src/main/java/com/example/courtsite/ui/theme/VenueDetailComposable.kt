package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsite.data.model.VenueDetail

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
            Text(detail.sportType, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
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
                modifier = Modifier.weight(1f)
            ) {
                Text("View")
            }
            Button(
                onClick = { onBookNow?.invoke() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Book Now")
            }
        }
    }
}

