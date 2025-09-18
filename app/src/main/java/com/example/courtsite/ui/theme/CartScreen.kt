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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// Simple in-memory cart
// Simple in-memory cart
object CartStore {
    val items: MutableList<CartItem> = mutableListOf()
}

data class CartItem(
    val venueName: String,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
    val court: Int,
    val price: Double,
    val sport: String
)

@Composable
fun CartScreen(navController: NavController? = null) {
    val count = CartStore.items.size
    val canProceed = count > 0
    val subtotal = CartStore.items.sumOf { it.price }
    val tax = subtotal * 0.08
    val total = subtotal + tax

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("My Cart", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("BOOKING SUMMARY", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            CartStore.items.forEach { item ->
                Surface(color = Color.White) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val dateFmt = item.date.format(DateTimeFormatter.ofPattern("d LLL yyyy, EEEE", Locale.ENGLISH))
                        val timeFmt = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH)
                        Column {
                            Text(dateFmt)
                            Text("${item.start.format(timeFmt)}-${item.end.format(timeFmt)} ${item.sport}")
                            Text("Court ${item.court}")
                        }
                        Text("RM ${String.format(Locale.ENGLISH, "%.2f", item.price)}")
                    }
                }
                Divider()
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
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("TOTAL", fontWeight = FontWeight.Bold)
                Text("RM ${String.format(Locale.ENGLISH, "%.2f", total)}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController?.navigate("payment-method") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Checkout Cart", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    CartStore.items.clear()
                    navController?.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    }
}

