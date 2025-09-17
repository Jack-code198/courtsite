package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import com.example.courtsite.utils.Validation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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


@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, String) -> Unit = { _, _, _ -> },
    onGoogleSignUp: () -> Unit = {},
    onFacebookSignUp: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    userExistsCheck: (String) -> Boolean = { false }, // Make sure this is NOT suspend
    onSignUpSuccess: (String) -> Unit = {}
) {
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "App Banner",
                modifier = Modifier
                    .size(width = 300.dp, height = 150.dp)
                    .padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign Up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Create your account using email.",
                color = Color(0xFF4E28CC),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) R.drawable.eye else R.drawable.eyeoff
                    Icon(
                        painter = painterResource(id = image),
                        tint = Color(0xFF4E28CC),
                        contentDescription = "Toggle Password Visibility",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { passwordVisible = !passwordVisible }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Text(
                text = "At least 8 characters, include uppercase, lowercase, number, special character. No spaces.",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) R.drawable.eye else R.drawable.eyeoff
                    Icon(
                        painter = painterResource(id = image),
                        tint = Color(0xFF4E28CC),
                        contentDescription = "Toggle Confirm Password Visibility",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
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
                            errorMessage = "User already exists. Please login."
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
                                            errorMessage = "Signup failed: no uid"
                                        }
                                    } else {
                                        errorMessage = task.exception?.localizedMessage ?: "Signup failed"
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4E28CC),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Text(
                buildAnnotatedString {
                    append("Already have an account? Click here to ")
                    withStyle(style = SpanStyle(color = Color(0xFF4E28CC), fontWeight = FontWeight.Bold)) {
                        append("log in")
                    }
                },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable { onLoginClick() },
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { onGoogleSignUp() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Google Sign Up",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { onFacebookSignUp() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.facebook_icon),
                    contentDescription = "Facebook Sign Up",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}