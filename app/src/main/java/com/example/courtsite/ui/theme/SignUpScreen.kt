package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import com.example.courtsite.utils.Validation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsite.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp


/**
 * A composable function that displays the Sign Up screen.
 * This screen allows users to create a new account with their email and password,
 * or through social media platforms like Google and Facebook.
 *
 * @param onSignUpClick Callback function triggered when the user clicks the sign-up button
 * @param onGoogleSignUp Callback function triggered when the user clicks on the Google sign-up button
 * @param onFacebookSignUp Callback function triggered when the user clicks on the Facebook sign-up button
 * @param onLoginClick Callback function triggered when the user clicks on the log-in link
 * @param userExistsCheck Function to check if the user already exists in the database
 * @param onSignUpSuccess Callback function triggered when the sign-up is successful
 */
@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, String) -> Unit = { _, _, _ -> },
    onLoginClick: () -> Unit = {},
    userExistsCheck: (String) -> Boolean = { false }, // Make sure this is NOT suspend
    onSignUpSuccess: (String) -> Unit = {}
) {
    // State variables for user input and UI states
    var emailOrPhone by remember { mutableStateOf("") } // User's email or phone input
    var password by remember { mutableStateOf("") } // User's password input
    var confirmPassword by remember { mutableStateOf("") } // User's password confirmation input
    var errorMessage by remember { mutableStateOf("") } // Error message to display
    var passwordVisible by remember { mutableStateOf(false) } // Flag to control password visibility
    var confirmPasswordVisible by remember { mutableStateOf(false) } // Flag to control confirm password visibility

    // Main container for the sign-up screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Upper section with form fields
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Top spacing

            // App banner image
            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "App Banner",
                modifier = Modifier
                    .size(width = 300.dp, height = 150.dp)
                    .padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp)) // Spacing below banner

            // Screen title
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Instruction text
            Text(
                text = "Fill in the details below to create your account",
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Email input field
            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                label = { Text("Email") },
                placeholder = { Text("Enter your email address") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4E28CC),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Create a password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    // Password visibility toggle icon
                    val image = if (passwordVisible) R.drawable.eye else R.drawable.eyeoff
                    Icon(
                        painter = painterResource(id = image),
                        tint = Color(0xFF4E28CC),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { passwordVisible = !passwordVisible }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4E28CC),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // Password requirements text
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Password requirements: At least 8 characters, include uppercase, lowercase, number, special character. No spaces.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            // Confirm password input field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Re-enter your password") },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    // Confirm password visibility toggle icon
                    val image = if (confirmPasswordVisible) R.drawable.eye else R.drawable.eyeoff
                    Icon(
                        painter = painterResource(id = image),
                        tint = Color(0xFF4E28CC),
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4E28CC),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // Error message display
            if (errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF0F0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Sign-up button
            Button(
                onClick = {
                    // First validate email/phone format
                    val inputError = Validation.getInputError(emailOrPhone)
                    when {
                        inputError != null -> {
                            errorMessage = inputError
                        }
                        Validation.getPasswordError(password) != null -> {
                            errorMessage = Validation.getPasswordError(password)!!
                        }
                        confirmPassword.isBlank() -> {
                            errorMessage = "Please confirm your password"
                        }
                        password != confirmPassword -> {
                            errorMessage = "Passwords do not match"
                        }
                        userExistsCheck(emailOrPhone) -> {
                            errorMessage = "This email is already registered. Please log in."
                        }
                        else -> {
                            errorMessage = ""
                            val auth = FirebaseAuth.getInstance()
                            auth.createUserWithEmailAndPassword(emailOrPhone, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val uid = auth.currentUser?.uid
                                        if (uid != null) {
                                            val db = FirebaseFirestore.getInstance()
                                            val userDoc = hashMapOf(
                                                "email" to emailOrPhone,
                                                "name" to null,
                                                "phone" to null,
                                                "createdAt" to Timestamp.now(),
                                                "updatedAt" to Timestamp.now()
                                            )
                                            db.collection("users").document(uid).set(userDoc)
                                                .addOnCompleteListener {
                                                    onSignUpSuccess(emailOrPhone)
                                                }
                                        } else {
                                            errorMessage = "Registration failed: Unable to get user ID"
                                        }
                                    } else {
                                        errorMessage = task.exception?.localizedMessage ?: "Registration failed, please try again later"
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4E28CC),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Login link
            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = Color(0xFF4E28CC), fontWeight = FontWeight.Bold)) {
                        append("Log In")
                    }
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable { onLoginClick() },
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        // Removed social sign-up buttons
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}