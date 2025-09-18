package com.example.courtsite.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
<<<<<<< HEAD
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
=======
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
import com.example.courtsite.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
=======
import com.example.courtsite.data.db.DatabaseProvider
import com.example.courtsite.data.model.User
import com.example.courtsite.data.session.SessionManager
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
<<<<<<< HEAD
    val currentUser = FirebaseAuth.getInstance().currentUser
=======
    val session = remember { SessionManager(context) }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
<<<<<<< HEAD
    var nameError by remember { mutableStateOf<String?>(null) }
    var profilePic by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (currentUser == null) return@LaunchedEffect
        
        try {
            val userDoc = withContext(Dispatchers.IO) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()
            }
            
            if (userDoc.exists()) {
                val userData = userDoc.data
                email = userData?.get("email") as? String ?: currentUser.email ?: ""
                phone = userData?.get("phone") as? String ?: ""
                profilePic = userData?.get("profilePicture") as? String
                name = userData?.get("name") as? String ?: currentUser.displayName ?: email.substringBefore('@')
            } else {
                // Use Firebase Auth data
                email = currentUser.email ?: ""
                phone = currentUser.phoneNumber ?: ""
                profilePic = currentUser.photoUrl?.toString()
                name = currentUser.displayName ?: email.substringBefore('@')
            }
        } catch (e: Exception) {
            // Fallback to Firebase Auth data
            email = currentUser.email ?: ""
            phone = currentUser.phoneNumber ?: ""
            profilePic = currentUser.photoUrl?.toString()
            name = currentUser.displayName ?: email.substringBefore('@')
=======
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
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
<<<<<<< HEAD
        uri?.let { selectedUri ->
            profilePic = selectedUri.toString()
            scope.launch {
                if (currentUser != null) {
                    try {
                        withContext(Dispatchers.IO) {
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(currentUser.uid)
                                .update("profilePicture", selectedUri.toString())
                                .await()
                        }
                    } catch (e: Exception) {
                        // Handle error if needed
=======
        uri?.let {
            profilePic = it.toString()
            scope.launch {
                session.getLoggedInUser()?.let { id ->
                    withContext(Dispatchers.IO) {
                        DatabaseProvider.getDatabase(context).userDao().updateProfilePicture(id, it.toString())
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
=======
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
                isError = nameError != null,
                supportingText = { if (nameError != null) Text(nameError!!, color = Color.Red, fontSize = 12.sp) },
=======
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
                    // Validate fields
                    var hasError = false
                    
                    // Validate name is not empty
                    nameError = if (name.isBlank()) "Name cannot be empty" else null
                    if (nameError != null) hasError = true
                    
                    // Validate phone number is not empty
                    if (phone.isBlank()) {
                        phoneError = "Phone number cannot be empty"
                        hasError = true
                    } else {
                        // Validate Malaysian phone number format
                        // Valid formats: +60123456789, 60123456789, or 0123456789
                        phoneError = if (!Regex("^(?:\\+?60|0)[1-9]\\d{8,9}$").matches(phone)) "Please enter a valid Malaysian phone number" else null
                        if (phoneError != null) hasError = true
                    }
                    
                    if (hasError) return@Button

                    // Save changes to Firestore
                    scope.launch {
                        if (currentUser != null) {
                            try {
                                withContext(Dispatchers.IO) {
                                    val updates = mutableMapOf<String, Any>()
                                    if (name.isNotBlank()) updates["name"] = name
                                    updates["email"] = email
                                    if (phone.isNotBlank()) updates["phone"] = phone
                                    
                                    FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(currentUser.uid)
                                        .update(updates)
                                        .await()
                                }
                            } catch (e: Exception) {
                                // Handle error if needed
=======
                    // Validate Malaysian phone number format
                    // Valid formats: +60123456789, 60123456789, or 0123456789
                    phoneError = if (phone.isNotBlank() && !Regex("^(?:\\+?60|0)[1-9]\\d{8,9}$").matches(phone)) "Please enter a valid Malaysian phone number" else null
                    if (phoneError != null) return@Button

                    // Save changes to database
                    scope.launch {
                        session.getLoggedInUser()?.let { id ->
                            withContext(Dispatchers.IO) {
                                DatabaseProvider.getDatabase(context).userDao().updateContact(id, if (name.isBlank()) null else name, email, if (phone.isBlank()) null else phone)
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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