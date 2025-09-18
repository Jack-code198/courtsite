package com.example.courtsite.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.courtsite.ui.theme.isValidContactInfo
import com.example.courtsite.ui.theme.validateFeedback
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Feedback
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

/**
 * A composable function that displays the Feedback screen.
 * This screen allows users to submit feedback or suggestions about the app.
 *
 * @param navController Navigation controller to handle screen navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavController) {
    // State variables for feedback form
    var feedbackText by remember { mutableStateOf("") }
    var feedbackType by remember { mutableStateOf("General") }
    var contactInfo by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) } // 0-5 rating
    val context = LocalContext.current

    // Main container for the feedback screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top app bar with back button
        TopAppBar(
            title = {
                Text(
                    text = "User Feedback",
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

        // Feedback type selection
        Text(
            text = "Feedback Type",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Feedback type options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeedbackTypeChip("Bug Report", feedbackType) { feedbackType = it }
            FeedbackTypeChip("Feature Request", feedbackType) { feedbackType = it }
            FeedbackTypeChip("General", feedbackType) { feedbackType = it }
        }

        // Rating section
        Text(
            text = "Rate Your Experience *",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Star rating component
            for (i in 1..5) {
                IconButton(
                    onClick = { rating = i },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Rate $i stars",
                        tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = when (rating) {
                    0 -> "No rating"
                    1 -> "Poor"
                    2 -> "Fair"
                    3 -> "Good"
                    4 -> "Very Good"
                    5 -> "Excellent"
                    else -> ""
                },
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }

        // Feedback description input
        OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Your Feedback *") },
            placeholder = { Text("Please describe your feedback in detail (minimum 10 characters)...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(150.dp),
            maxLines = 5,
            isError = feedbackText.isNotBlank() && feedbackText.length < 10
        )

        // Character count and error message
        if (feedbackText.isNotBlank() && feedbackText.length < 10) {
            Text(
                text = "Feedback must be at least 10 characters (${feedbackText.length}/10)",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }

        // Contact information input (optional but validated if provided)
        OutlinedTextField(
            value = contactInfo,
            onValueChange = { contactInfo = it },
            label = { Text("Contact Information (Optional)") },
            placeholder = { Text("Email or phone number if you'd like a response") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            isError = contactInfo.isNotBlank() && !isValidContactInfo(contactInfo)
        )

        // Contact info validation error message
        if (contactInfo.isNotBlank() && !isValidContactInfo(contactInfo)) {
            Text(
                text = "Please enter a valid email address or phone number",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        // Submit button
        Button(
            onClick = {
                val (isValid, errorMessage) = validateFeedback(feedbackText, rating, contactInfo)
                if (isValid) {
                    submitFeedback(feedbackType, feedbackText, contactInfo, rating, context)
                    Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E28CC)
            )
        ) {
            Text("Submit Feedback")
        }
    }
}

/**
 * A composable function that displays a feedback type selection chip.
 *
 * @param type The type of feedback this chip represents
 * @param selectedType The currently selected feedback type
 * @param onTypeSelected Callback function when this chip is selected
 */
@Composable
fun FeedbackTypeChip(type: String, selectedType: String, onTypeSelected: (String) -> Unit) {
    FilterChip(
        selected = type == selectedType,
        onClick = { onTypeSelected(type) },
        label = { Text(type) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF4E28CC),
            selectedLabelColor = Color.White
        )
    )
}

/**
 * Submits the feedback to be stored locally.
 * In a production app, this would typically send the feedback to a server.
 *
 * @param type The type of feedback
 * @param feedbackText The content of the feedback
 * @param contactInfo User's contact information
 * @param rating User's rating (0-5 stars)
 * @param context Android context for accessing SharedPreferences
 */
private fun submitFeedback(type: String, feedbackText: String, contactInfo: String, rating: Int, context: Context) {
    // Get SharedPreferences for storing feedback locally
    val prefs = context.getSharedPreferences("UserFeedback", Context.MODE_PRIVATE)
    val editor = prefs.edit()

    // Create a unique key using timestamp
    val timestamp = System.currentTimeMillis()

    // Store feedback data
    editor.putString("feedback_${timestamp}_type", type)
    editor.putString("feedback_${timestamp}_text", feedbackText)
    editor.putString("feedback_${timestamp}_contact", contactInfo)
    editor.putInt("feedback_${timestamp}_rating", rating)
    editor.putLong("feedback_${timestamp}_time", timestamp)

    // Apply changes
    editor.apply()

    // In a real app, you would also send this to your backend:
    // Firebase.firestore.collection("feedback").add(mapOf(
    //     "type" to type,
    //     "text" to feedbackText,
    //     "contact" to contactInfo,
    //     "timestamp" to timestamp,
    //     "userId" to FirebaseAuth.getInstance().currentUser?.uid
    // ))
}
