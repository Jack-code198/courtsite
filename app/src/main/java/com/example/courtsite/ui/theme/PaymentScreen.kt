package com.example.courtsite.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun PaymentScreen(navController: NavController? = null) {
    val subtotal = CartStore.items.sumOf { it.price }
    val tax = subtotal * 0.08
    val total = subtotal + tax

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("My Cart", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("BOOKING SUMMARY", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            // Simplified summary; images and address omitted per request
            CartStore.items.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${item.start} - ${item.end}  ${item.sport}")
                    Text("RM ${String.format(Locale.ENGLISH, "%.2f", item.price)}")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("PRICE SUMMARY", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal")
                Text("RM ${String.format(Locale.ENGLISH, "%.2f", subtotal)}")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tax (8% SST)")
                Text("RM ${String.format(Locale.ENGLISH, "%.2f", tax)}")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("TOTAL", fontWeight = FontWeight.Bold)
                Text("RM ${String.format(Locale.ENGLISH, "%.2f", total)}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: proceed payment */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Checkout Cart", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController?.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit", color = Color.Black)
            }
        }
    }
}

