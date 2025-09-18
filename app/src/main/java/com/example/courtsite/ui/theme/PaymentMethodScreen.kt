package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale
import com.example.courtsite.R
import androidx.compose.ui.platform.LocalContext
import com.example.courtsite.data.session.SessionManager
import java.time.Duration

enum class PaymentMethod {
    FPX, VISA_MASTERCARD, E_WALLETS
}

@Composable
fun PaymentMethodScreen(navController: NavController? = null) {
    val subtotal = CartStore.items.sumOf { it.price }
    val tax = subtotal * 0.08
    val total = subtotal + tax
    
    var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Select a payment method",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Payment method options
            PaymentMethodOption(
                method = PaymentMethod.FPX,
                title = "Online Banking",
                subtitle = "Pay with Online Banking",
                isSelected = selectedMethod == PaymentMethod.FPX,
                onClick = { selectedMethod = PaymentMethod.FPX }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PaymentMethodOption(
                method = PaymentMethod.VISA_MASTERCARD,
                title = "Visa / Mastercard",
                subtitle = "Credit/Debit Card",
                isSelected = selectedMethod == PaymentMethod.VISA_MASTERCARD,
                onClick = { selectedMethod = PaymentMethod.VISA_MASTERCARD }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PaymentMethodOption(
                method = PaymentMethod.E_WALLETS,
                title = "e-Wallets",
                subtitle = "Boost, GrabPay, Touch 'n GO",
                isSelected = selectedMethod == PaymentMethod.E_WALLETS,
                onClick = { selectedMethod = PaymentMethod.E_WALLETS }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Price summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel and Pay Now buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel button
                Button(
                    onClick = { showCancelDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", color = Color.Black, fontSize = 16.sp)
                }

                // Pay Now button
                Button(
                    onClick = { 
                        // Update booking stats BEFORE clearing the cart
                        val bookingCount = CartStore.items.size
                        val totalHours = CartStore.items
                            .map { Duration.between(it.start, it.end).toMinutes().toFloat() / 60f }
                            .fold(0f) { acc, h -> acc + h }
                        sessionManager.addBookingStats(bookingCount, totalHours, total.toFloat())

                        // Mark bookings as unavailable and show success dialog
                        BookingStore.markBookingsAsUnavailable(CartStore.items)
                        CartStore.items.clear()
                        showSuccessDialog = true
                    },
                    enabled = selectedMethod != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedMethod != null) Color(0xFF4E28CC) else Color(0xFFEDEDED),
                        contentColor = if (selectedMethod != null) Color.White else Color(0xFFB0B3BA)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pay Now", fontSize = 16.sp)
                }
            }
        }
    }

    // Payment success dialog
    if (showSuccessDialog) {
        PaymentSuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                // Navigate back to home tab
                navController?.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }

    // Cancel confirmation dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        // Clear all cart items and return to home
                        CartStore.items.clear()
                        showCancelDialog = false
                        navController?.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCancelDialog = false }) {
                    Text("Cancel")
                }
            },
            title = {
                Text(
                    "Cancel Booking?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to cancel all current bookings? This action cannot be undone.")
            }
        )
    }
}

@Composable
private fun PaymentMethodOption(
    method: PaymentMethod,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .border(
                2.dp,
                if (isSelected) Color(0xFF4E28CC) else Color(0xFFE0E0E0),
                RoundedCornerShape(12.dp)
            )
            .background(if (isSelected) Color(0xFFF0EDFF) else Color.White),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Payment method icons/logos with actual images
                when (method) {
                    PaymentMethod.FPX -> {
                        Image(
                            painter = painterResource(id = R.drawable.fpx),
                            contentDescription = "FPX",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                    PaymentMethod.VISA_MASTERCARD -> {
                        Image(
                            painter = painterResource(id = R.drawable.visa),
                            contentDescription = "Credit/Debit Card",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                    PaymentMethod.E_WALLETS -> {
                        Image(
                            painter = painterResource(id = R.drawable.wallet),
                            contentDescription = "e-Wallets",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color(0xFF4E28CC) else Color.Black
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Selection indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF4E28CC),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun PaymentSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC))
            ) {
                Text("OK", color = Color.White)
            }
        },
        title = {
            Text(
                "Payment Successful",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                "Your booking has been confirmed! You will receive a confirmation email shortly.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
