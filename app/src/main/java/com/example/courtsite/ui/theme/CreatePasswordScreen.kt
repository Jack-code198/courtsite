package com.example.courtsite.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import com.example.courtsite.utils.Validation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePasswordScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val identifier = currentUser?.email

    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPwd by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Password") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White)
        ) {
            // Info banner
            AssistChip(
                onClick = {},
                label = { Text("After creating your password, you can log in next time using your current email and this password.") },
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
                enabled = false,
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = Color(0xFFEFF2FF),
                    disabledLabelColor = Color(0xFF2C2C2C)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Current email (read-only)
            Text("Current email")
            OutlinedTextField(
                value = identifier ?: "",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Password
            Text("Password")
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPwd) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPwd = !showPwd }) {
                        Icon(if (showPwd) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null)
                    }
                },
                placeholder = { Text("Enter password") },
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "At least 8 characters, include uppercase, lowercase, number, special character. No spaces.",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Confirm password
            Text("Confirm password")
            OutlinedTextField(
                value = confirm,
                onValueChange = { confirm = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showConfirm = !showConfirm }) {
                        Icon(if (showConfirm) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null)
                    }
                },
                placeholder = { Text("Re-enter your password") },
                shape = RoundedCornerShape(12.dp)
            )

            if (errorText != null) {
                Spacer(Modifier.height(12.dp))
                Text(errorText!!, color = Color.Red)
            }
            if (success) {
                Spacer(Modifier.height(12.dp))
                Text("Password created successfully.", color = Color(0xFF4E28CC))
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    errorText = null
                    if (identifier.isNullOrBlank()) {
                        errorText = "No current email. Please login first."
                        return@Button
                    }
                    Validation.getPasswordError(password)?.let {
                        errorText = it
                        return@Button
                    }
                    if (confirm.isBlank()) {
                        errorText = "Please confirm your password"
                        return@Button
                    }
                    if (password != confirm) {
                        errorText = "Passwords do not match."
                        return@Button
                    }
                    scope.launch {
                        try {
                            // Update password in Firebase Auth
                            currentUser?.updatePassword(password)?.await()
                            
                            // Update password in Firestore (optional, as Firebase Auth handles this)
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(currentUser.uid)
                                .update("password", password)
                                .await()
                            
                            success = true
                        } catch (e: Exception) {
                            errorText = e.localizedMessage ?: "Failed to update password"
                        }
                    }
                },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC))
            ) {
                Text("Create Password")
            }
        }
    }
}


