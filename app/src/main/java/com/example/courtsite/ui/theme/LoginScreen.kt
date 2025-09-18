package com.example.courtsite.ui.theme

import android.widget.Toast
import com.example.courtsite.utils.Validation
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

/**
 * A composable function that displays the Login screen.
 * This screen allows users to log in with their email and password,
 * or through social media platforms like Google and Facebook.
 *
 * @param onSignUpClick Callback function triggered when the user clicks on the sign-up link
 * @param onGoogleLogin Callback function triggered when the user clicks on the Google login button
 * @param onFacebookLogin Callback function triggered when the user clicks on the Facebook login button
 * @param userExistsCheck Function to check if the user exists in the database
 * @param onLoginSuccess Callback function triggered when the login is successful
 * @param onForgotPasswordClick Callback function triggered when the user clicks on the "Forgot password" link
 */
@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit = {},
    userExistsCheck: (String, String) -> Boolean, // Remove suspend keyword
    onLoginSuccess: (String) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    // State variables for user input and UI states
    var identifier by remember { mutableStateOf("") } // User's email or phone input
    var password by remember { mutableStateOf("") } // User's password input
    var passwordVisible by remember { mutableStateOf(false) } // Flag to control password visibility
    var errorMessage by remember { mutableStateOf("") } // Error message to display
    val context = LocalContext.current // Get the current context for Toast messages
    // Remove the scope since we don't need coroutines anymore

    // Main container for the login screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // Top spacing

        // App banner image
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "App Banner",
            modifier = Modifier.size(width = 300.dp, height = 150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp)) // Spacing below banner

        // Screen title
        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Instruction text
        Text(
            text = "Sign in to your account to continue",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Email input field
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
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
            placeholder = { Text("Enter your password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // Password visibility toggle icon
                val icon = if (passwordVisible) R.drawable.eye else R.drawable.eyeoff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = description,
                        tint = Color(0xFF4E28CC),
                        modifier = Modifier.size(24.dp)
                    )
                }
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

        // Forgot password link
        Text(
            text = "Forgot Password?",
            color = Color(0xFF4E28CC),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
                .clickable { onForgotPasswordClick() }
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

        // Login button
        Button(
            onClick = {
                // First validate email/phone format
                val inputError = Validation.getInputError(identifier)
                when {
                    inputError != null -> {
                        errorMessage = inputError
                    }
                    password.isBlank() -> {
                        errorMessage = "Password is required"
                    }
                    else -> {
                        errorMessage = ""
                        val auth = FirebaseAuth.getInstance()
                        auth.signInWithEmailAndPassword(identifier, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(identifier)
                                } else {
                                    val msg = task.exception?.localizedMessage
                                        ?: "Invalid credentials, please check your email and password"
                                    errorMessage = msg
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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
                text = "Sign In",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Sign-up link
        Text(
            buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF4E28CC),
                        fontWeight = FontWeight.Bold
                    )
                ) { append("Sign Up") }
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable { onSignUpClick() },
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp)) // Spacing before social login buttons

        // Removed social login buttons
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onSignUpClick = {},
        userExistsCheck = { _, _ -> false }
    )
}