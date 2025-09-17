package com.example.courtsite.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.courtsite.utils.Validation
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(navController: NavController? = null) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Forgot your password?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "We'll email you instructions to reset your password. If we can't find our email, please check your spam or junk.",
            color = Color(0xFF333333),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(24.dp))

        Text(text = "Email *", color = Color.Black, fontSize = 14.sp)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(text = error!!, color = Color.Red, fontSize = 13.sp)
        }
        if (success) {
            Spacer(Modifier.height(8.dp))
            Text(text = "If the email exists, reset instructions have been sent.", color = Color(0xFF4E28CC), fontSize = 13.sp)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                error = Validation.getInputError(email)
                if (error == null) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            success = task.isSuccessful
                            if (!task.isSuccessful) {
                                error = task.exception?.localizedMessage ?: "Failed to send email"
                            } else {
                                error = null
                            }
                        }
                } else {
                    success = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC))
        ) {
            Text("Send reset instructions")
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Return to Log In",
            color = Color(0xFF4E28CC),
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable { navController?.popBackStack() }
        )
    }
}


