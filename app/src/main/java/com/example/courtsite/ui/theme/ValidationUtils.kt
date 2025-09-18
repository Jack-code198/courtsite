package com.example.courtsite.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Validates if the contact information is a valid email or phone number.
 *
 * @param contactInfo The contact information to validate
 * @return Boolean indicating if the contact info is valid
 */
fun isValidContactInfo(contactInfo: String): Boolean {
    // Email regex pattern - more comprehensive
    val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"

    // Phone number regex pattern - accepts various formats including international
    val phonePattern = "^(?:(?:\\+(?:\\d{1,3}[- ]?)?)?(?:\\(?\\d{3}\\)?[- ]?)?\\d{3}[- ]?\\d{3}[- ]?\\d{4})$"

    return contactInfo.matches(emailPattern.toRegex()) || 
           contactInfo.matches(phonePattern.toRegex())
}

/**
 * Validates the feedback input to ensure required fields are filled and contact info is valid.
 *
 * @param feedbackText The feedback text to validate
 * @param rating The user rating to validate
 * @param contactInfo The contact information to validate
 * @return Boolean indicating if the feedback is valid
 * @return String containing error message if validation fails, empty otherwise
 */
fun validateFeedback(feedbackText: String, rating: Int, contactInfo: String): Pair<Boolean, String> {
    when {
        feedbackText.isBlank() -> return Pair(false, "Please enter your feedback before submitting")
        feedbackText.length < 10 -> return Pair(false, "Feedback must be at least 10 characters long")
        rating == 0 -> return Pair(false, "Please provide a rating before submitting")
        contactInfo.isNotBlank() && !isValidContactInfo(contactInfo) -> 
            return Pair(false, "Please enter a valid email address or phone number")
        else -> return Pair(true, "")
    }
}
