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
<<<<<<< HEAD
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
=======
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
    userExistsCheck: (String, String) -> Boolean, // Remove suspend keyword
    onLoginSuccess: (String) -> Unit = {}
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    // Remove the scope since we don't need coroutines anymore

>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
<<<<<<< HEAD
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // Top spacing

        // App banner image
=======
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "App Banner",
            modifier = Modifier.size(width = 300.dp, height = 150.dp)
        )

<<<<<<< HEAD
        Spacer(modifier = Modifier.height(24.dp)) // Spacing below banner

        // Screen title
        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
=======
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Login / Registration",
            fontSize = 22.sp,
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

<<<<<<< HEAD
        // Instruction text
        Text(
            text = "Sign in to your account to continue",
            color = Color.Gray,
            fontSize = 16.sp,
=======
        Text(
            text = "Enter your email and password.",
            color = Color(0xFF4E28CC),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

<<<<<<< HEAD
        // Email input field
=======
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Email") },
<<<<<<< HEAD
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
=======
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
<<<<<<< HEAD
            placeholder = { Text("Enter your password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // Password visibility toggle icon
=======
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
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
=======
                .padding(vertical = 8.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
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
=======
                        val exists = userExistsCheck(identifier, password)
                        if (exists) {
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(identifier)
                        } else {
                            errorMessage = "Invalid credentials. Please check your email/phone and password."
                            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
<<<<<<< HEAD
                .padding(top = 24.dp)
                .height(56.dp),
=======
                .padding(top = 16.dp),
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E28CC),
                contentColor = Color.White
            ),
<<<<<<< HEAD
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
=======
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Text(
            buildAnnotatedString {
                append("Don't have an account? Click here to ")
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF4E28CC),
                        fontWeight = FontWeight.Bold
                    )
<<<<<<< HEAD
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
=======
                ) { append("sign-up") }
            },
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable { onSignUpClick() },
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { onGoogleLogin() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Google Login",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { onFacebookLogin() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.facebook_icon),
                    contentDescription = "Facebook Login",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onSignUpClick = {},
<<<<<<< HEAD
=======
        onGoogleLogin = {},
        onFacebookLogin = {},
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        userExistsCheck = { _, _ -> false }
    )
}