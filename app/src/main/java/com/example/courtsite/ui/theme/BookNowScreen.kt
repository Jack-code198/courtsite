package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import com.example.courtsite.R
import java.net.URLDecoder
import java.net.URLEncoder
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNowScreen(
    navController: NavController? = null,
    venueName: String,
    sportType: String
){
    val decodedName = try { URLDecoder.decode(venueName, "UTF-8") } catch (e: Exception) { venueName }
    val decodedSport = try { URLDecoder.decode(sportType, "UTF-8") } catch (e: Exception) { sportType }

    // Ensure global booking context is set so downstream screens (availability/cart/payment) use same venue
    LaunchedEffect(decodedName, decodedSport) {
        BookingData.setCurrentVenueByName(decodedName)
        BookingData.setCurrentSport(decodedSport)
    }

    // Track cart state for reactive updates
    val cartState = remember { mutableStateOf(CartStore.items.toList()) }

    // State for showing venue layout
    val showVenueLayout = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(decodedName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(decodedSport, fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Step states
                val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
                val showDateDialog = remember { mutableStateOf(false) }
                val currentMonth = remember { mutableStateOf(YearMonth.now()) }
                val selectedTime = remember { mutableStateOf<LocalTime?>(null) }
                val selectedDurationHrs = remember { mutableStateOf<Int?>(null) }
                val showTimeDialog = remember { mutableStateOf(false) }

                // Toggle header (Calendar vs Classic)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            val encodedName = URLEncoder.encode(decodedName, "UTF-8")
                            val encodedSport = URLEncoder.encode(decodedSport, "UTF-8")
                            // Replace current screen with Availability so back doesn't bounce between screens
                            navController?.navigate("availability/$encodedName/$encodedSport") {
                                popUpTo("booknow/$encodedName/$encodedSport") { inclusive = true }
                            }
                        },
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Calendar booking",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Calendar booking")
                    }

                    Spacer(Modifier.size(12.dp))

                    Button(
                        onClick = { /* already here */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4E28CC),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SportsTennis,
                            contentDescription = "Classic booking",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Classic booking")
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Choose your preferred date, time and court",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9EA3AE)
                )

                Spacer(Modifier.height(24.dp))

                // Date selection field
                val dateLabel = selectedDate.value?.format(
                    DateTimeFormatter.ofPattern("d LLL yyyy, EEEE", Locale.ENGLISH)
                ) ?: "Select date"
                SelectableField(
                    isSelected = selectedDate.value != null,
                    label = dateLabel,
                    onClick = { showDateDialog.value = true }
                )

                if (showDateDialog.value) {
                    CalendarDialog(
                        month = currentMonth.value,
                        onPrevMonth = { currentMonth.value = currentMonth.value.minusMonths(1) },
                        onNextMonth = { currentMonth.value = currentMonth.value.plusMonths(1) },
                        onDismiss = { showDateDialog.value = false },
                        onDateSelected = { date ->
                            selectedDate.value = date
                            showDateDialog.value = false
                            selectedTime.value = null
                            selectedDurationHrs.value = null
                        }
                    )
                }

                Spacer(Modifier.size(12.dp))

                // Time selection field
                val timeLabel = when {
                    selectedDate.value == null -> "Select date first"
                    selectedTime.value == null -> "Select start time"
                    else -> selectedTime.value!!.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
                }
                SelectableField(
                    isSelected = selectedTime.value != null,
                    label = timeLabel,
                    onClick = {
                        if (selectedDate.value != null) {
                            showTimeDialog.value = true
                        }
                    }
                )

                // Time selection dialog
                if (showTimeDialog.value) {
                    TimeSelectionDialog(
                        onDismiss = { showTimeDialog.value = false },
                        onTimeSelected = { time ->
                            selectedTime.value = time
                            showTimeDialog.value = false
                        },
                        // You would pass available times from your availability screen here
                        availableTimes = getAvailableTimesForDate(selectedDate.value)
                    )
                }

                Spacer(Modifier.size(12.dp))

                // Duration selection field
                val durationLabel = when {
                    selectedTime.value == null -> "Select time first"
                    selectedDurationHrs.value == null -> "Select duration"
                    else -> "${selectedDurationHrs.value} hr${if (selectedDurationHrs.value!! > 1) "s" else ""}"
                }
                SelectableField(
                    isSelected = selectedDurationHrs.value != null,
                    label = durationLabel,
                    onClick = {
                        if (selectedTime.value != null) {
                            // Show duration options
                            selectedDurationHrs.value = 1 // Default duration
                        }
                    }
                )

                // Duration options (shown when time is selected)
                if (selectedTime.value != null) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Select available duration",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        (1..4).forEach { h ->
                            val isSelected = selectedDurationHrs.value == h
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFF0EDFF) else Color.White)
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(0xFF4E28CC) else Color(0xFFD1D1D1),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedDurationHrs.value = h }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "$h hr${if (h > 1) "s" else ""}",
                                    color = if (isSelected) Color(0xFF4E28CC) else Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Venue Layout link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Venue Layout",
                        color = Color.Blue,
                        style = androidx.compose.ui.text.TextStyle(
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .clickable { showVenueLayout.value = true }
                            .padding(8.dp)
                    )
                }

                Text(
                    text = "Select court(s)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(8.dp))

                if (selectedTime.value != null && selectedDurationHrs.value != null) {
                    // Show available courts
                    val courts = listOf("Court 1", "Court 2", "Court 3", "Court 4")
                    val times = BookingData.timeSlots()
                    val startIndex = times.indexOf(selectedTime.value!!)
                    val endIndexExclusive = times.indexOf(selectedTime.value!!.plusHours(selectedDurationHrs.value!!.toLong()))
                    courts.forEach { court ->
                        val courtNumber = courts.indexOf(court) + 1
                        val isAvailableForRange = BookingData.isCourtAvailableRange(
                            venueName = decodedName,
                            date = selectedDate.value!!,
                            court = courtNumber,
                            startIndex = startIndex,
                            endIndexExclusive = endIndexExclusive,
                            times = times
                        )
                        if (!isAvailableForRange) return@forEach
                        CourtCard(
                            courtName = court,
                            sportType = decodedSport,
                            price = BookingData.getVenueHourlyRate(decodedName) * selectedDurationHrs.value!!,
                            isSelected = CartStore.items.any { it.court == courtNumber && it.venueName == decodedName },
                            onAddToCart = {
                                val start = selectedTime.value!!
                                val end = start.plusHours(selectedDurationHrs.value!!.toLong())

                                // Remove if already exists
                                CartStore.items.removeIf { item ->
                                    item.venueName == decodedName &&
                                        item.court == courtNumber &&
                                        item.date == selectedDate.value &&
                                        item.start == start
                                }

                                // Add to cart
                                CartStore.items.add(
                                    CartItem(
                                        venueName = decodedName,
                                        date = selectedDate.value!!,
                                        start = start,
                                        end = end,
                                        court = courtNumber,
                                        price = BookingData.getVenueHourlyRate(decodedName) * selectedDurationHrs.value!!,
                                        sport = decodedSport
                                    )
                                )
                                cartState.value = CartStore.items.toList()
                            },
                            onRemoveFromCart = {
                                CartStore.items.removeIf { item ->
                                    item.venueName == decodedName &&
                                        item.court == courtNumber &&
                                        item.date == selectedDate.value &&
                                        item.start == selectedTime.value
                                }
                                cartState.value = CartStore.items.toList()
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F5F5))
                            .height(70.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "No available court(s)",
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Bottom summary bar - This will now reactively update when cartState changes
                val sum = cartState.value.sumOf { it.price }
                val count = cartState.value.size
                val canProceed = count > 0

                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        val priceText = "RM " + String.format(Locale.ENGLISH, "%.2f", sum)
                        val slotText = "$count ${if (count == 1) "court" else "courts"}"
                        Text(text = priceText, fontWeight = FontWeight.Bold)
                        Text(text = slotText, color = Color(0xFF9EA3AE))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        // Cancel All button
                        Surface(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.LightGray)
                                    .clickable {
                                        CartStore.items.clear()
                                        cartState.value = CartStore.items.toList()
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Cancel All",
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        val nextBg = if (canProceed) Color(0xFF4E28CC) else Color(0xFFEDEDED)
                        val nextFg = if (canProceed) Color.White else Color(0xFFB0B3BA)
                        Surface(
                            color = nextBg,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(nextBg)
                                    .clickable(enabled = canProceed) {
                                        navController?.navigate("cart")
                                    }
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Next",
                                    color = nextFg,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Venue Layout Overlay
            if (showVenueLayout.value) {
                VenueLayoutOverlay(
                    onDismiss = { showVenueLayout.value = false }
                )
            }
        }
    }
}

// Add these new composables for time selection and venue layout

@Composable
fun TimeSelectionDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    availableTimes: List<LocalTime>
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Select Time")
        },
        text = {
            Column {
                Text("Available times:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // Display available times in a grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableTimes.size) { index ->
                        val time = availableTimes[index]
                        val formattedTime = time.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF0EDFF))
                                .border(1.dp, Color(0xFF4E28CC), RoundedCornerShape(8.dp))
                                .clickable {
                                    onTimeSelected(time)
                                    onDismiss()
                                }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(formattedTime, color = Color(0xFF4E28CC))
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun VenueLayoutOverlay(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp)
                .padding(16.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                // Close button at the top right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Venue Layout",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Venue layout image
                    Image(
                        painter = painterResource(id = R.drawable.venue_layout_sample),
                        contentDescription = "Venue Layout",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Court layout details
                    Text(
                        text = "Court Layout:",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "â€¢ Professional courts\nâ€¢ Rest areas\nâ€¢ Equipment rental\nâ€¢ Changing facilities",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4E28CC),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Close", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

// Helper function to get available times with max booking time of 2:00 AM
private fun getAvailableTimesForDate(date: LocalDate?): List<LocalTime> {
    // Define available times from 9 AM to 2 AM (next day)
    val times = mutableListOf<LocalTime>()

    // Regular hours: 9 AM to 11 PM (23:00)
    for (hour in 9..23) {
        times.add(LocalTime.of(hour, 0))
    }

    // Late night hours: 12 AM to 2 AM (00:00 to 02:00)
    for (hour in 0..2) {
        times.add(LocalTime.of(hour, 0))
    }

    return times.sorted()
}

@Composable
private fun SelectableField(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF4E28CC) else Color(0xFFD1D1D1),
            modifier = Modifier.size(24.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = Color(0x40000000),
                    spotColor = Color(0x40000000)
                )
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFFF0EDFF) else Color.White)
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color(0xFF4E28CC) else Color(0xFFD1D1D1),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) Color(0xFF4E28CC) else Color.Black
            )
        }
    }
}

@Composable
private fun CourtCard(
    courtName: String,
    sportType: String,
    price: Double,
    isSelected: Boolean,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(courtName, fontWeight = FontWeight.Bold)
                Text(sportType, color = Color.Gray, fontSize = 12.sp)
                Text("RM ${String.format(Locale.ENGLISH, "%.2f", price)}")
            }

            if (isSelected) {
                Button(
                    onClick = onRemoveFromCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4E28CC),
                        contentColor = Color.White
                    )
                ) {
                    Text("Added to Cart")
                }
            } else {
                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4E28CC),
                        contentColor = Color.White
                    )
                ) {
                    Text("Add to Cart")
                }
            }
        }
    }
}

@Composable
private fun CalendarDialog(
    month: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Previous month",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onPrevMonth() }
                )
                Text(
                    text = month.month.name.lowercase().replaceFirstChar { it.titlecase(Locale.ENGLISH) } +
                            " " + month.year,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next month",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onNextMonth() }
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Weekdays header
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("Su","Mo","Tu","We","Th","Fr","Sa").forEach { d ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(d, color = Color.Gray)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                val firstOfMonth = month.atDay(1)
                val daysInMonth = month.lengthOfMonth()
                val firstDayOfWeekIndex = ((firstOfMonth.dayOfWeek.value % 7)) // Sunday = 0

                val totalCells = firstDayOfWeekIndex + daysInMonth
                val weeks = (totalCells + 6) / 7

                var dayCounter = 1
                repeat(weeks) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (i in 0 until 7) {
                            val cellIndex = it * 7 + i
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (cellIndex < firstDayOfWeekIndex || dayCounter > daysInMonth) {
                                    Text("")
                                } else {
                                    val date = month.atDay(dayCounter)
                                    val isPast = date.isBefore(today)
                                    val textColor = if (isPast) Color(0xFFB0B3BA) else Color.Black
                                    Text(
                                        text = dayCounter.toString(),
                                        color = textColor,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .clickable(enabled = !isPast) { onDateSelected(date) }
                                            .padding(8.dp)
                                    )
                                    dayCounter++
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = "Booking opens at 12:00 AM daily — book up to 14 days ahead.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    )
}