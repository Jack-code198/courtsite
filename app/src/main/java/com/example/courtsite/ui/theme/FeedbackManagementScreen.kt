package com.example.courtsite.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.courtsite.ui.theme.isValidContactInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

/**
 * A data class to represent a user feedback item
 */
data class FeedbackItem(
    val id: String,
    val type: String,
    val text: String,
    val contact: String,
    val rating: Int,
    val timestamp: Long,
    var status: String = "pending" // pending, reviewed, resolved
)

/**
 * A composable function that displays the Feedback Management screen.
 * This screen allows administrators to view and manage user feedback.
 *
 * @param navController Navigation controller to handle screen navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackManagementScreen(navController: NavController) {
    val context = LocalContext.current
    var feedbackList by remember { mutableStateOf(loadFeedbacks(context)) }
    var selectedFilter by remember { mutableStateOf("all") }

    // Filter feedback based on selection
    val filteredFeedback = when (selectedFilter) {
        "pending" -> feedbackList.filter { it.status == "pending" }
        "reviewed" -> feedbackList.filter { it.status == "reviewed" }
        "resolved" -> feedbackList.filter { it.status == "resolved" }
        else -> feedbackList
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top app bar with back button
        TopAppBar(
            title = { 
                Text(
                    text = "Feedback Management",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ) 
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text("All (${feedbackList.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF4E28CC),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == "pending",
                onClick = { selectedFilter = "pending" },
                label = { Text("Pending (${feedbackList.count { it.status == "pending" }})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF4E28CC),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == "reviewed",
                onClick = { selectedFilter = "reviewed" },
                label = { Text("Reviewed (${feedbackList.count { it.status == "reviewed" }})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF4E28CC),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == "resolved",
                onClick = { selectedFilter = "resolved" },
                label = { Text("Resolved (${feedbackList.count { it.status == "resolved" }})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF4E28CC),
                    selectedLabelColor = Color.White
                )
            )
        }

        // Feedback list
        if (filteredFeedback.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No feedback found",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFeedback) { feedback ->
                    FeedbackItemCard(
                        feedback = feedback,
                        onStatusChange = { newStatus ->
                            feedbackList = feedbackList.map {
                                if (it.id == feedback.id) {
                                    updateFeedbackStatus(context, it.id, newStatus)
                                    it.copy(status = newStatus)
                                } else it
                            }
                        },
                        onDelete = {
                            feedbackList = feedbackList.filter { it.id != feedback.id }
                            deleteFeedback(context, feedback.id)
                        }
                    )
                }
            }
        }
    }
}

/**
 * A composable function that displays a single feedback item in a card.
 *
 * @param feedback The feedback item to display
 * @param onStatusChange Callback when the status of the feedback changes
 * @param onDelete Callback when the feedback is deleted
 */
@Composable
fun FeedbackItemCard(
    feedback: FeedbackItem,
    onStatusChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (feedback.status) {
                "pending" -> Color(0xFFFFF8E1) // Light yellow
                "reviewed" -> Color(0xFFE3F2FD) // Light blue
                "resolved" -> Color(0xFFE8F5E9) // Light green
                else -> Color.White
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Feedback type and status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = feedback.type,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E28CC)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = when (feedback.status) {
                            "pending" -> Color(0xFFFFC107)
                            "reviewed" -> Color(0xFF2196F3)
                            "resolved" -> Color(0xFF4CAF50)
                            else -> Color.Gray
                        }
                    ) {
                        Text(
                            text = feedback.status.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // Star rating display
                    Spacer(modifier = Modifier.width(12.dp))
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < feedback.rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (index < feedback.rating) Color(0xFFFFC107) else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Date
                Text(
                    text = formatTimestamp(feedback.timestamp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Feedback text
            Text(
                text = feedback.text,
                modifier = Modifier.fillMaxWidth()
            )

            // Contact info if available
            if (feedback.contact.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Contact: ${feedback.contact}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Status change buttons
                if (feedback.status != "reviewed") {
                    TextButton(
                        onClick = { onStatusChange("reviewed") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailRead,
                            contentDescription = "Mark as reviewed",
                            tint = Color(0xFF2196F3)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mark as Reviewed")
                    }
                }

                if (feedback.status != "resolved") {
                    TextButton(
                        onClick = { onStatusChange("resolved") }
                    ) {
                        Text("Mark as Resolved")
                    }
                }

                // Delete button
                IconButton(
                    onClick = { onDelete() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete feedback",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

/**
 * Loads all feedbacks from SharedPreferences
 *
 * @param context Android context for accessing SharedPreferences
 * @return List of FeedbackItem objects
 */
fun loadFeedbacks(context: Context): List<FeedbackItem> {
    val prefs = context.getSharedPreferences("UserFeedback", Context.MODE_PRIVATE)
    val allEntries = prefs.all
    val feedbackList = mutableListOf<FeedbackItem>()

    // Group related entries by ID
    val feedbackMap = mutableMapOf<String, MutableMap<String, Any?>>()

    allEntries.forEach { entry ->
        val key = entry.key
        if (key.startsWith("feedback_")) {
            val parts = key.split("_")
            if (parts.size >= 3) {
                val id = parts[1]
                val field = parts[2]

                if (!feedbackMap.containsKey(id)) {
                    feedbackMap[id] = mutableMapOf()
                }

                feedbackMap[id]?.put(field, entry.value)
            }
        }
    }

    // Convert map to FeedbackItem objects
    feedbackMap.forEach { (id, fields) ->
        val type = fields["type"] as? String ?: "General"
        val text = fields["text"] as? String ?: ""
        val contact = fields["contact"] as? String ?: ""
        val rating = fields["rating"] as? Int ?: 0
        val timestamp = fields["time"] as? Long ?: System.currentTimeMillis()
        val status = fields["status"] as? String ?: "pending"

        feedbackList.add(
            FeedbackItem(
                id = id,
                type = type,
                text = text,
                contact = contact,
                rating = rating,
                timestamp = timestamp,
                status = status
            )
        )
    }

    // Sort by timestamp (newest first)
    return feedbackList.sortedByDescending { it.timestamp }
}

/**
 * Updates the status of a feedback in SharedPreferences
 *
 * @param context Android context for accessing SharedPreferences
 * @param feedbackId The ID of the feedback to update
 * @param newStatus The new status value
 */
fun updateFeedbackStatus(context: Context, feedbackId: String, newStatus: String) {
    val prefs = context.getSharedPreferences("UserFeedback", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString("feedback_${feedbackId}_status", newStatus)
    editor.apply()
}

/**
 * Deletes a feedback from SharedPreferences
 *
 * @param context Android context for accessing SharedPreferences
 * @param feedbackId The ID of the feedback to delete
 */
fun deleteFeedback(context: Context, feedbackId: String) {
    val prefs = context.getSharedPreferences("UserFeedback", Context.MODE_PRIVATE)
    val editor = prefs.edit()

    // Remove all entries related to this feedback
    editor.remove("feedback_${feedbackId}_type")
    editor.remove("feedback_${feedbackId}_text")
    editor.remove("feedback_${feedbackId}_contact")
    editor.remove("feedback_${feedbackId}_time")
    editor.remove("feedback_${feedbackId}_status")

    editor.apply()
}

/**
 * Formats a timestamp into a readable date string
 *
 * @param timestamp The timestamp to format
 * @return Formatted date string
 */
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
