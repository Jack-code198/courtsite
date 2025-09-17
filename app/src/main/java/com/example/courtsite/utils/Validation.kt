package com.example.courtsite.utils

object Validation {
    // Check if the input is a valid Malaysian phone number
    // Returns true if the number matches Malaysian format:
    // - Can start with +60, 60, or 0
    // - Followed by 1-9 (first digit after prefix)
    // - Then 8-9 more digits
    fun isValidMalaysianPhone(phone: String): Boolean {
        val regex = Regex("^(?:\\+?60|0)[1-9]\\d{8,9}$")
        return regex.matches(phone)
    }

    // Validates email address with strict rules
    // Returns true only if the email matches all validation criteria:
    // 1. Username part: allows letters, numbers, dots, underscores, and hyphens
    // 2. Must have @ symbol
    // 3. Domain part: allows letters, numbers, and hyphens
    // 4. Must end with valid top-level domain (.com, .org, etc.)
    fun isValidEmail(email: String): Boolean {
        // Common top-level domains
        val validTlds = listOf(
            "com", "org", "net", "edu", "gov", "mil",
            "biz", "info", "pro", "name", "museum", "int",
            "aero", "coop", "jobs", "mobi", "travel"
        )

        // Basic format check using regex
        val basicFormatRegex = Regex("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        if (!basicFormatRegex.matches(email)) {
            return false
        }

        // Additional validation checks
        val parts = email.split("@")
        if (parts.size != 2) return false

        val username = parts[0]
        val domain = parts[1]

        // Username specific checks
        if (username.startsWith(".") ||
            username.endsWith(".") ||
            username.contains("..")) {
            return false
        }

        // Domain specific checks
        val domainParts = domain.split(".")
        if (domainParts.size < 2) return false

        // Get the top-level domain (last part)
        val tld = domainParts.last().lowercase()

        // Check if it's a valid top-level domain
        return validTlds.contains(tld)
    }

    // Get specific error message based on the validation result
    // Returns null if the input is valid, otherwise returns an error message
    fun getInputError(input: String): String? {
        return when {
            input.isBlank() -> "This field is required"
            !isValidEmail(input) -> "Please enter a valid email address"
            else -> null
        }
    }
}