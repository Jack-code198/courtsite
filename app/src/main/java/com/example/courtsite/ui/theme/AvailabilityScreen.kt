package com.example.courtsite.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLDecoder
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(navController: NavController? = null, venueName: String = "", sportType: String = "") {
    val decodedName = try { URLDecoder.decode(venueName, "UTF-8") } catch (e: Exception) { venueName }
    val decodedSport = try { URLDecoder.decode(sportType, "UTF-8") } catch (e: Exception) { sportType }

    val sportOptions = listOf("Badminton", "Futsal", "Tennis")
    val rentalOptions = listOf("Badminton Court Rental", "Racket Rental", "Shuttlecock Rental")

    val selectedSport = remember { mutableStateOf(if (decodedSport.isNotBlank()) decodedSport else "Badminton") }
    // Set current booking context
    LaunchedEffect(decodedName, selectedSport.value) {
        BookingData.setCurrentVenueByName(decodedName)
        BookingData.setCurrentSport(selectedSport.value)
    }
    val selectedRental = remember { mutableStateOf("Badminton Court Rental") }

    val sportMenuExpanded = remember { mutableStateOf(false) }
    val rentalMenuExpanded = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val showDateDialog = remember { mutableStateOf(false) }
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }

    // Track cart state for reactive updates
    val cartState = remember { mutableStateOf(CartStore.items.toList()) }
    // Signal child grid to clear transient selections on Cancel All
    val clearSignal = remember { mutableStateOf(0) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Segmented-like header buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // already on calendar booking
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4E28CC),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Calendar booking",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Calendar booking")
                    }
                }

                Spacer(modifier = Modifier.size(12.dp))

                OutlinedButton(
                    onClick = {
                        val encodedName = java.net.URLEncoder.encode(decodedName, "UTF-8")
                        val encodedSport = java.net.URLEncoder.encode(selectedSport.value, "UTF-8")
                        // Replace current screen with BookNow so back goes to previous non-booking screen
                        navController?.navigate("booknow/$encodedName/$encodedSport") {
                            popUpTo("availability/$encodedName/$encodedSport") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.SportsTennis,
                            contentDescription = "Classic booking",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Classic booking")
                    }
                }
            }

            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Select your slot directly from the calendar",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9EA3AE)
            )

            Spacer(modifier = Modifier.size(24.dp))

            // Selectable row: Sport
            SelectableField(
                isSelected = selectedSport.value != "Select sport",
                label = selectedSport.value,
                onClick = { sportMenuExpanded.value = true }
            )

            DropdownMenu(
                expanded = sportMenuExpanded.value,
                onDismissRequest = { sportMenuExpanded.value = false }
            ) {
                sportOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedSport.value = option
                            sportMenuExpanded.value = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Selectable row: Rental Type
            SelectableField(
                isSelected = selectedRental.value != "Select rental",
                label = selectedRental.value,
                onClick = { rentalMenuExpanded.value = true }
            )
            DropdownMenu(
                expanded = rentalMenuExpanded.value,
                onDismissRequest = { rentalMenuExpanded.value = false }
            ) {
                rentalOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedRental.value = option
                            rentalMenuExpanded.value = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Date row: opens calendar dialog
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
                    }
                )
            }

            if (selectedDate.value != null) {
                Spacer(modifier = Modifier.size(16.dp))
                TimeSlotsGrid(
                    selectedDate = selectedDate.value!!,
                    clearSignal = clearSignal.value,
                    venueName = decodedName,
                    onAddToCart = { courtIndex, startIndex, endIndex, price, startTime, endTime ->
                        // Save to cart store for cart screen
                        CartStore.items.add(
                            CartItem(
                                venueName = decodedName,
                                date = selectedDate.value!!,
                                start = startTime,
                                end = endTime,
                                court = courtIndex + 1,
                                price = price,
                                sport = selectedSport.value
                            )
                        )
                        // Update the cart state to trigger recomposition
                        cartState.value = CartStore.items.toList()
                    },
                    onCartChanged = {
                        cartState.value = CartStore.items.toList()
                    }
                )
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
                                    clearSignal.value = clearSignal.value + 1
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
    }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportSelectionSheet(
    sports: List<String>,
    onSportSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select Sport",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            sports.forEach { sport ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSportSelected(sport) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsTennis,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = sport,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Divider()
            }
        }
    }
}

@Composable
private fun TimeSlotsGrid(
    selectedDate: LocalDate,
    clearSignal: Int,
    venueName: String,
    onAddToCart: (courtIndex: Int, startIndex: Int, endIndex: Int, price: Double, startTime: LocalTime, endTime: LocalTime) -> Unit,
    onCartChanged: () -> Unit
) {
    val times = remember { BookingData.timeSlots() }
    val numCourts = 4
    val selected = remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val headerHeight = 40.dp
    val rowHeight = 48.dp
    val timeColumnWidth = 72.dp

    val showDurationSheet = remember { mutableStateOf(false) }
    val selectedStartTime = remember { mutableStateOf<LocalTime?>(null) }
    val selectedCourt = remember { mutableStateOf<Int?>(null) }
    val selectedStartIndex = remember { mutableStateOf<Int?>(null) }
    val showDeleteDialog = remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val previewRange = remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val previewCourt = remember { mutableStateOf<Int?>(null) }

    // Clear transient UI selections when parent emits a new signal (e.g., Cancel All)
    androidx.compose.runtime.LaunchedEffect(clearSignal) {
        selected.value = null
        selectedStartTime.value = null
        selectedCourt.value = null
        selectedStartIndex.value = null
        previewRange.value = null
        previewCourt.value = null
        showDurationSheet.value = false
        showDeleteDialog.value = null
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDot(background = Color(0xFFB8B9C1), content = "Unavailable")
                LegendDot(background = Color.White, border = Color.LightGray, content = "Available")
                LegendDot(background = Color(0xFF4E28CC), content = "Selected")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // Left labels column
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(90.dp)
                ) {
                    Spacer(modifier = Modifier.height(headerHeight))
                    repeat(numCourts) { idx ->
                        Box(
                            modifier = Modifier
                                .height(rowHeight)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = "Court ${idx + 1}")
                        }
                    }
                }

                // Scrollable time columns
                val listState = rememberLazyListState()
                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(times.size) { tIndex ->
                        val time = times[tIndex]
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(timeColumnWidth)
                        ) {
                            Box(
                                modifier = Modifier.height(headerHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                TimeHeader(time)
                            }
                            repeat(numCourts) { cIndex ->
                                val key = tIndex to cIndex
                                val isUnavailable = (cIndex == 0 && tIndex % 4 == 0) || (cIndex == 2 && tIndex % 7 == 3) ||
                                        BookingData.isSlotBooked(selectedDate, cIndex + 1, tIndex, times, venueName = venueName)
                                val isInCart = BookingData.isInCartAt(selectedDate, cIndex + 1, tIndex, times, venueName = venueName)
                                val isCommittedSelected = selected.value == key || isInCart
                                val isInPreview = previewCourt.value == (cIndex + 1) &&
                                        (previewRange.value?.let { tIndex >= it.first && tIndex < it.second } ?: false)
                                val isSelected = isCommittedSelected || isInPreview
                                Box(
                                    modifier = Modifier.height(rowHeight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SlotCircle(
                                        unavailable = isUnavailable,
                                        selected = isSelected,
                                        onClick = {
                                            if (isInCart) {
                                                showDeleteDialog.value = key
                                            } else if (!isUnavailable) {
                                                selected.value = if (isSelected) null else key
                                                selectedStartTime.value = time
                                                selectedCourt.value = cIndex + 1
                                                selectedStartIndex.value = tIndex
                                                previewCourt.value = cIndex + 1
                                                previewRange.value = null
                                                showDurationSheet.value = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDurationSheet.value && selectedStartTime.value != null && selectedCourt.value != null && selectedStartIndex.value != null) {
        DurationSheet(
            courtNumber = selectedCourt.value!!,
            date = selectedDate,
            startTime = selectedStartTime.value!!,
            startIndex = selectedStartIndex.value!!,
            times = times,
            venueName = venueName,
            isUnavailableAt = { timeIdx, courtIdx ->
                val c = courtIdx
                val t = timeIdx
                (c == 0 && t % 4 == 0) || (c == 2 && t % 7 == 3) ||
                        BookingData.isSlotBooked(selectedDate, c + 1, t, times, venueName = venueName)
            },
            onPreview = { endIndexExclusive ->
                previewRange.value = selectedStartIndex.value?.let { it to endIndexExclusive }
                previewCourt.value = selectedCourt.value
            },
            onAddToCart = { endIndexExclusive, price ->
                val startIdx = selectedStartIndex.value!!
                val sTime = times[startIdx]
                val eTime = times[endIndexExclusive]
                onAddToCart(selectedCourt.value!! - 1, startIdx, endIndexExclusive, price, sTime, eTime)
                showDurationSheet.value = false
                previewRange.value = null
                previewCourt.value = null
            },
            onDismiss = {
                showDurationSheet.value = false
                previewRange.value = null
                previewCourt.value = null
            }
        )
    }

    // Delete confirmation dialog
    val keyToDelete = showDeleteDialog.value
    if (keyToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = null },
            confirmButton = {
                Button(onClick = {
                    // Remove matching booking from CartStore (the one covering this slot)
                    val removed = CartStore.items.removeIf { item ->
                        if (item.date != selectedDate) return@removeIf false
                        if (item.court != keyToDelete.second + 1) return@removeIf false
                        val startIdx = times.indexOf(item.start)
                        val endIdx = times.indexOf(item.end)
                        keyToDelete.first in startIdx until endIdx
                    }
                    if (removed) {
                        onCartChanged()
                    }
                    showDeleteDialog.value = null
                }) { Text("Remove selection") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog.value = null }) { Text("Cancel") }
            },
            title = { Text("Remove selected slot?") },
            text = { Text("This slot is already selected. Do you want to remove it?") }
        )
    }
}

@Composable
private fun LegendDot(background: Color, content: String, border: Color? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(50))
                .background(background)
                .then(if (border != null) Modifier.border(1.dp, border, RoundedCornerShape(50)) else Modifier)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = content)
    }
}

@Composable
private fun SlotCircle(unavailable: Boolean, selected: Boolean, onClick: () -> Unit) {
    val bg = when {
        unavailable -> Color(0xFFD9D9DF)
        selected -> Color(0xFF4E28CC)
        else -> Color.White
    }
    val borderColor = if (unavailable) Color(0xFFD9D9DF) else Color.LightGray
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(50))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(50))
            .clickable(enabled = !unavailable) { onClick() }
    )
}

@Composable
private fun TimeHeader(time: LocalTime) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val labelTop = time.format(DateTimeFormatter.ofPattern("h:mm"))
        val labelBottom = time.format(DateTimeFormatter.ofPattern("a")).uppercase(Locale.ENGLISH)
        Text(text = labelTop, fontWeight = FontWeight.SemiBold)
        Text(text = labelBottom, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    }
}

private fun generateHalfHourRange(): List<LocalTime> {
    val result = mutableListOf<LocalTime>()
    var minutesFromStart = 0
    val totalMinutes = ((17) * 60) // 9:00 AM to 2:00 AM next day is 17 hours
    val start = LocalTime.of(9, 0)
    while (minutesFromStart <= totalMinutes) {
        result.add(start.plusMinutes(minutesFromStart.toLong()))
        minutesFromStart += 30
    }
    return result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationSheet(
    courtNumber: Int,
    date: LocalDate,
    startTime: LocalTime,
    startIndex: Int,
    times: List<LocalTime>,
    venueName: String,
    isUnavailableAt: (timeIndex: Int, courtIndex: Int) -> Boolean,
    onPreview: (endIndexExclusive: Int) -> Unit,
    onAddToCart: (endIndexExclusive: Int, price: Double) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val endLimit = LocalTime.of(20, 0) // 8:00 PM boundary

    val options = remember(startTime, startIndex, courtNumber, times) {
        val list = mutableListOf<Pair<LocalTime, LocalTime>>()
        // find the furthest contiguous available index from start, stopping before first unavailable
        var endIdx = startIndex
        // minimum 1h => +2 slots required
        var minEndIdx = (startIndex + 2)
        // walk forward while next slot start is available and time < 8:00 PM
        while (endIdx + 1 < times.size) {
            val nextTime = times[endIdx + 1]
            if (nextTime.isAfter(endLimit)) break
            if (isUnavailableAt(endIdx + 1, courtNumber - 1)) break
            endIdx += 1
            // hard cap: max 4 hours from start = 8 slots beyond start
            if (endIdx - startIndex >= 8) break
        }
        // Build ranges from 1 hour up to the found end index
        val maxEndIdx = minOf(endIdx, startIndex + 8) // 4h cap
        for (idx in minEndIdx..maxEndIdx) {
            val end = times[idx]
            list.add(startTime to end)
        }
        list
    }

    val selectedEndIdx = remember { mutableStateOf<Int?>(null) }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(text = "Select duration", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "COURT $courtNumber", color = Color(0xFF4E28CC), fontWeight = FontWeight.Bold)
            Text(
                text = date.format(DateTimeFormatter.ofPattern("d LLL yyyy, EEEE", Locale.ENGLISH)).uppercase(Locale.ENGLISH),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            options.forEachIndexed { idx, (start, end) ->
                val endIndexExclusive = times.indexOf(end)
                val isSelectedOption = selectedEndIdx.value == endIndexExclusive
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val halfHours = (java.time.Duration.between(start, end).toMinutes() / 30).toInt()
                            val price = 40.0 + (halfHours - 2).coerceAtLeast(0) * 20.0
                            onPreview(endIndexExclusive)
                            selectedEndIdx.value = endIndexExclusive
                        }
                        .border(
                            1.dp,
                            if (isSelectedOption) Color(0xFF4E28CC) else Color(0xFFD1D1D1),
                            RoundedCornerShape(12.dp)
                        )
                        .background(if (isSelectedOption) Color(0xFFF0EDFF) else Color.White),
                    color = Color.Transparent,
                    tonalElevation = 0.dp,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val timeFmt = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
                        val label = "${start.format(timeFmt)} - ${end.format(timeFmt)}"
                        val hours = ((java.time.Duration.between(start, end).toMinutes()).toDouble() / 60.0)
                        val hoursLabel = if (hours % 1.0 == 0.0) "${hours.toInt()} hr" else "${hours} hr"
                        Text(text = "$label ($hoursLabel)", color = if (isSelectedOption) Color(0xFF4E28CC) else Color.Black)
                        val price = BookingData.computePrice(start, end, venueName)
                        Text(text = "RM ${String.format(Locale.ENGLISH, "%.2f", price)}", color = if (isSelectedOption) Color(0xFF4E28CC) else Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Add to Cart button appears after a preview selection is made
            Button(
                onClick = {
                    val endIdx = selectedEndIdx.value
                    if (endIdx != null) {
                        val start = times[startIndex]
                        val end = times[endIdx]
                        val price = BookingData.computePrice(start, end, venueName = venueName)
                        onAddToCart(endIdx, price)
                    }
                },
                enabled = selectedEndIdx.value != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart", color = Color.White)
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
                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
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