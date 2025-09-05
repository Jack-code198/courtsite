package com.example.courtsite.ui.theme

import android.widget.Toast
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "App Banner",
            modifier = Modifier.size(width = 300.dp, height = 150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Login / Registration",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Enter your email or phone and password.",
            color = Color(0xFF4E28CC),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Email / Phone") },
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
                if (identifier.isBlank() || password.isBlank()) {
                    errorMessage = "All fields are required"
                } else {
                    errorMessage = ""
                    // Call the function directly (no coroutine needed)
                    val exists = userExistsCheck(identifier, password)
                    if (exists) {
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                        onLoginSuccess(identifier)
                    } else {
                        errorMessage = "Invalid credentials. Please check your email/phone and password."
                        Toast.makeText(context, "Invalid credentials. Please check your email/phone and password.", Toast.LENGTH_SHORT).show()
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
                text = "Login",
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Text(
            buildAnnotatedString {
                append("Don't have an account? Click here to ")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF4E28CC),
                        fontWeight = FontWeight.Bold
                    )
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
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onSignUpClick = {},
        onGoogleLogin = {},
        onFacebookLogin = {},
        userExistsCheck = { _, _ -> false }
    )
}


