package com.example.courtsite.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.courtsite.R
import com.example.courtsite.data.db.DatabaseProvider
import com.example.courtsite.data.model.User
import com.example.courtsite.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var profilePic by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val identifier = session.getLoggedInUser()
        if (!identifier.isNullOrBlank()) {
            user = withContext(Dispatchers.IO) {
                DatabaseProvider.getDatabase(context).userDao().findUserByIdentifier(identifier)
            }
            user?.let {
                email = it.email ?: ""
                phone = it.phone ?: ""
                profilePic = it.profilePicture
                name = (it.email ?: it.phone ?: "User").substringBefore('@')
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profilePic = it.toString()
            scope.launch {
                session.getLoggedInUser()?.let { id ->
                    withContext(Dispatchers.IO) {
                        DatabaseProvider.getDatabase(context).userDao().updateProfilePicture(id, it.toString())
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF4E28CC))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("AVATAR", fontSize = 12.sp, color = Color(0xFF888888), fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = if (!profilePic.isNullOrEmpty()) rememberAsyncImagePainter(profilePic) else painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp).clip(CircleShape).border(3.dp, Color(0xFFEDEDED), CircleShape).background(Color.LightGray, CircleShape)
                )
                Spacer(Modifier.width(16.dp))
                FilledTonalButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Icon(painterResource(id = R.drawable.camera), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Edit Photo")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("PERSONAL", fontSize = 12.sp, color = Color(0xFF888888), fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Text("CONTACT", fontSize = 12.sp, color = Color(0xFF888888), fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { }, // Disable email editing
                label = { Text("Email") },
                enabled = false, // Disable input field
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Mobile number") },
                isError = phoneError != null,
                supportingText = { if (phoneError != null) Text(phoneError!!, color = Color.Red, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    // Validate Malaysian phone number format
                    // Valid formats: +60123456789, 60123456789, or 0123456789
                    phoneError = if (phone.isNotBlank() && !Regex("^(?:\\+?60|0)[1-9]\\d{8,9}$").matches(phone)) "Please enter a valid Malaysian phone number" else null
                    if (phoneError != null) return@Button

                    // Save changes to database
                    scope.launch {
                        session.getLoggedInUser()?.let { id ->
                            withContext(Dispatchers.IO) {
                                DatabaseProvider.getDatabase(context).userDao().updateContact(id, if (name.isBlank()) null else name, email, if (phone.isBlank()) null else phone)
                            }
                        }
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC))
            ) {
                Text("Update Account")
            }
        }
    }
}